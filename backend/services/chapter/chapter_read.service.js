import { sequelize } from '../../models/index.js';
import ApiError from '../../utils/api_error.util.js';
import {
  validateChapter,
  canUserAccessChapter,
} from '../../utils/chapter.util.js';
import { handleTransaction } from '../../utils/handle_transaction.util.js';
import { formatDate } from '../../utils/date.util.js';

const Story = sequelize.models.Story;
const Chapter = sequelize.models.Chapter;
const History = sequelize.models.History;

const ChapterReadingService = {
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
        History.upsert(
          { userId, storyId: chapter.storyId, lastReadAt: new Date() },
          { transaction }
        ),
        chapter.increment('viewNum', { transaction }),
        Story.increment('viewNum', {
          where: { storyId: chapter.story.storyId },
          transaction,
        }),
      ]);

      const chapterResult = chapter.toJSON();
      chapterResult.updatedAt = formatDate(chapter.updatedAt);

      return chapterResult;
    });
  },

  async readNextChapter(currentChapterId, userId) {
    const currentChapter = await validateChapter(currentChapterId, true);

    const nextChapter = await Chapter.findOne({
      where: {
        storyId: currentChapter.storyId,
        ordinalNumber: currentChapter.ordinalNumber + 1,
      },
      include: [
        {
          model: Story,
          as: 'story',
        },
      ],
    });

    if (!nextChapter) {
      throw new ApiError('Chương tiếp theo không tồn tại', 404);
    }

    // Kiểm tra quyền truy cập nếu chương tiếp theo bị khóa
    if (nextChapter.lockedStatus && userId !== nextChapter.story.userId) {
      const canAccess = await canUserAccessChapter(userId, nextChapter);
      if (!canAccess) {
        throw new ApiError('Bạn cần mua chương này để đọc', 403);
      }
    }

    await nextChapter.increment('viewNum');
    await Story.increment('viewNum', {
      where: { storyId: nextChapter.storyId },
    });

    const chapterResult = nextChapter.toJSON();
    chapterResult.updatedAt = formatDate(nextChapter.updatedAt);
    chapterResult.canAccess = true;

    return chapterResult;
  },
};

export default ChapterReadingService;
