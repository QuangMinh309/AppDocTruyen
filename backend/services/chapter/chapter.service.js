import { sequelize } from '../../models/index.js';
import ApiError from '../../utils/api_error.util.js';
import {
  validateChapter,
  canUserAccessChapter,
  validateSortParams,
} from '../../utils/chapter.util.js';
import { validateStory } from '../../utils/story.util.js';
import { handleTransaction } from '../../utils/handle_transaction.util.js';
import { formatDate } from '../../utils/date.util.js';
import { Op } from 'sequelize';
import NotificationService from '../notification.service.js';

const Story = sequelize.models.Story;
const Chapter = sequelize.models.Chapter;
const Comment = sequelize.models.Comment;
const Purchase = sequelize.models.Purchase;

const ChapterService = {
  async createChapter(chapterData, userId) {
    const { storyId } = chapterData;
    await validateStory(
      storyId,
      userId,
      'Bạn không có quyền tạo chương cho truyện này'
    );

    return await handleTransaction(async (transaction) => {
      const story = await Story.findOne({
        where: { storyId },
        attributes: ['pricePerChapter', 'storyName', 'userId'],
        transaction,
      });

      if (!story) {
        throw new ApiError('Không tìm thấy truyện', 404);
      }

      const shouldLock = story.pricePerChapter > 0;

      // Lấy danh sách ordinalNumber hiện có
      const ordinals = await Chapter.findAll({
        where: { storyId },
        attributes: ['ordinalNumber'],
        transaction,
      }).then((chapters) =>
        chapters.map((chapter) => chapter.ordinalNumber).sort((a, b) => a - b)
      );

      // Tìm ordinalNumber nhỏ nhất chưa sử dụng
      let nextOrdinal = 1;
      for (const ordinal of ordinals) {
        if (ordinal > nextOrdinal) break;
        nextOrdinal = ordinal + 1;
      }

      const chapter = await Chapter.create(
        {
          ...chapterData,
          storyId: parseInt(storyId),
          ordinalNumber: nextOrdinal,
          viewNum: 0,
          lockedStatus: nextOrdinal === 1 ? false : shouldLock,
          updatedAt: new Date(),
        },
        { transaction }
      );

      await Story.increment('chapterNum', {
        where: { storyId },
        transaction,
      });

      const message = `Tác giả bạn theo dõi vừa đăng chương mới cho truyện: ${story.storyName}`;
      await NotificationService.notifyFollowers(
        story.userId, // tác giả
        storyId,
        message,
        transaction
      );

      const chapterResult = chapter.toJSON();
      chapterResult.storyId = parseInt(chapterResult.storyId);
      chapterResult.updatedAt = formatDate(chapter.updatedAt);

      return chapterResult;
    });
  },

  async getChapterById(chapterId, userId = null) {
    const chapter = await validateChapter(chapterId, true);
    if (userId) {
      chapter.dataValues.canAccess = await canUserAccessChapter(
        userId,
        chapter
      );
    }
    const chapterResult = chapter.toJSON();
    chapterResult.updatedAt = formatDate(chapter.updatedAt);

    return chapterResult;
  },

  async getChaptersByStory(
    storyId,
    userId = null,
    limit = 20,
    lastId = null,
    orderBy = 'ordinalNumber',
    sort = 'ASC'
  ) {
    try {
      await validateStory(storyId);
      const validOrderFields = ['ordinalNumber', 'createdAt', 'updatedAt'];
      const { orderBy: finalOrderBy, sort: finalSort } = validateSortParams(
        orderBy,
        sort,
        validOrderFields
      );

      const where = lastId
        ? {
            storyId,
            chapterId: {
              [finalSort === 'DESC' ? Op.lt : Op.gt]: lastId,
            },
          }
        : { storyId };

      const chapters = await Chapter.findAll({
        where,
        limit: parseInt(limit),
        order: [[finalOrderBy, finalSort]],
        attributes: { exclude: ['content'] },
        include: [
          {
            model: Story,
            as: 'story',
            attributes: ['storyId', 'storyName'],
          },
        ],
      });

      if (userId) {
        for (const chapter of chapters) {
          chapter.dataValues.canAccess = await canUserAccessChapter(
            userId,
            chapter
          );
        }
      }

      for (const chapter of chapters) {
        chapter.dataValues.updatedAt = formatDate(chapter.updatedAt);
      }

      const nextLastId =
        chapters.length > 0 ? chapters[chapters.length - 1].chapterId : null;

      return {
        chapters,
        nextLastId,
        hasMore: chapters.length === parseInt(limit),
      };
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi lấy danh sách chương', 500);
    }
  },

  async updateChapter(chapterId, chapterData, userId) {
    const chapter = await validateChapter(chapterId, true);
    await validateStory(
      chapter.story.storyId,
      userId,
      'Bạn không có quyền sửa chương này'
    );
    return await chapter.update({ ...chapterData, updatedAt: new Date() });
  },

  async deleteChapter(chapterId, userId) {
    return await handleTransaction(async (transaction) => {
      // console.log(`Attempting to delete chapter with ID: ${chapterId}`);
      const chapter = await validateChapter(chapterId, true);
      await validateStory(
        chapter.story.storyId,
        userId,
        'Bạn không có quyền xóa chương này'
      );

      // Xóa các comment và purchase liên quan
      await Promise.all([
        Comment.destroy({ where: { chapterId }, transaction }),
        Purchase.destroy({ where: { chapterId }, transaction }),
      ]);

      // Xóa chapter
      await chapter.destroy({ transaction });
      // console.log(`Chapter ${chapterId} deleted from database`);

      // Giảm chapterNum trong Story
      await Story.decrement('chapterNum', {
        where: { storyId: chapter.story.storyId },
        transaction,
      });
      // console.log(`Decremented chapterNum for story ${chapter.story.storyId}`);

      // Cập nhật ordinalNumber cho các chapter còn lại
      const remainingChapters = await Chapter.findAll({
        where: {
          storyId: chapter.story.storyId,
          ordinalNumber: { [Op.gt]: chapter.ordinalNumber },
        },
        order: [['ordinalNumber', 'ASC']],
        transaction,
      });

      for (const ch of remainingChapters) {
        await ch.update(
          { ordinalNumber: ch.ordinalNumber - 1 },
          { transaction }
        );
        // console.log(
        //   `Updated ordinalNumber for chapter ${ch.chapterId} to ${
        //     ch.ordinalNumber - 1
        //   }`
        // );
      }

      // Xóa cache nếu có (giả định sử dụng Redis)
      try {
        const redis = sequelize.models.redis; // Giả định bạn có Redis client trong sequelize.models
        if (redis) {
          await redis.del(`story:${chapter.story.storyId}`);
          // console.log(`Cleared cache for story ${chapter.story.storyId}`);
        }
      } catch (cacheError) {
        console.warn(`Failed to clear cache: ${cacheError.message}`);
      }

      return {
        success: true,
        message: 'Xóa chương thành công',
      };
    });
  },
};

export default ChapterService;
