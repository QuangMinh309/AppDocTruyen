import { sequelize } from '../../models/index.js';
import ApiError from '../../utils/api_error.util.js';
import {
  validateChapter,
  canUserAccessChapter,
  handlePurchaseTransaction,
} from '../../utils/chapter.util.js';
import { validateStory } from '../../utils/story.util.js';
import { handleTransaction } from '../../utils/handle_transaction.util.js';
import { formatDate } from '../../utils/date.util.js';

const Story = sequelize.models.Story;
const Chapter = sequelize.models.Chapter;
const History = sequelize.models.History;

const ChapterService = {
  async createChapter(chapterData, userId) {
    const { storyId } = chapterData;
    await validateStory(
      storyId,
      userId,
      'Bạn không có quyền tạo chương cho truyện này'
    );

    return await handleTransaction(async (transaction) => {
      const shouldLock = await Story.findOne({
        where: { storyId },
        attributes: ['pricePerChapter'],
        transaction,
      }).then((story) => story.pricePerChapter > 0);

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

      const chapterResult = chapter.toJSON();
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

  async readChapter(chapterId, userId) {
    return await handleTransaction(async (transaction) => {
      const chapter = await validateChapter(chapterId, true);
      if (chapter.lockedStatus && userId !== chapter.story.userId) {
        const canAccess = await canUserAccessChapter(userId, chapter);
        if (!canAccess) {
          throw new ApiError('Bạn cần mua chương này để đọc', 403);
        }
      }

      await Promise.all([
        sequelize.models.History.upsert(
          { userId, storyId: chapter.storyId, lastReadAt: new Date() },
          { transaction }
        ),
        chapter.increment('viewNum', { transaction }),
        sequelize.models.Story.increment('viewNum', {
          where: { storyId: chapter.story.storyId },
          transaction,
        }),
      ]);

      const chapterResult = chapter.toJSON();
      chapterResult.updatedAt = formatDate(chapter.updatedAt);

      return chapterResult;
    });
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
      const chapter = await validateChapter(chapterId, true);
      await validateStory(
        chapter.story.storyId,
        userId,
        'Bạn không có quyền xóa chương này'
      );

      await Promise.all([
        sequelize.models.Comment.destroy({ where: { chapterId }, transaction }),
        sequelize.models.Purchase.destroy({
          where: { chapterId },
          transaction,
        }),
        sequelize.models.History.destroy({ where: { chapterId }, transaction }),
        chapter.destroy({ transaction }),
        sequelize.models.Story.decrement('chapterNum', {
          where: { storyId: chapter.story.storyId },
          transaction,
        }),
      ]);

      const remainingChapters = await sequelize.models.Chapter.findAll({
        where: {
          storyId: chapter.story.storyId,
          ordinalNumber: { [sequelize.Sequelize.Op.gt]: chapter.ordinalNumber },
        },
        order: [['ordinalNumber', 'ASC']],
        transaction,
      });

      for (const ch of remainingChapters) {
        await ch.update(
          { ordinalNumber: ch.ordinalNumber - 1 },
          { transaction }
        );
      }

      return true;
    });
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
              [finalSort === 'DESC'
                ? sequelize.Sequelize.Op.lt
                : sequelize.Sequelize.Op.gt]: lastId,
            },
          }
        : { storyId };

      const chapters = await sequelize.models.Chapter.findAll({
        where,
        limit: parseInt(limit),
        order: [[finalOrderBy, finalSort]],
        include: [
          {
            model: sequelize.models.Story,
            as: 'story',
            attributes: ['storyId', 'storyName', 'userId'],
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

  async purchaseChapter(userId, storyId, chapterId) {
    return await handleTransaction(async (transaction) => {
      const chapter = await validateChapter(chapterId, true);
      return await handlePurchaseTransaction(
        userId,
        chapter,
        storyId,
        transaction
      );
    });
  },
};

export default ChapterService;
