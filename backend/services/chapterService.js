import { sequelize } from "../models/index.js";
import ApiError from "../utils/apiError.js";
import {
  validateChapter,
  canUserAccessChapter,
  getChaptersByStory,
  handlePurchaseTransaction,
} from "../utils/chapterUtils.js";
import { validateStory } from "../utils/storyUtils.js";
import { handleTransaction } from "../utils/transactionUtils.js";

const ChapterService = {
  async createChapter(chapterData, userId) {
    const { storyId } = chapterData;
    await validateStory(
      storyId,
      userId,
      "Bạn không có quyền tạo chương cho truyện này"
    );

    return await handleTransaction(async (transaction) => {
      const shouldLock = await sequelize.models.Story.findOne({
        where: { storyId },
        attributes: ["price", "pricePerChapter"],
        transaction,
      }).then((story) => story.price > 0 || story.pricePerChapter > 0);

      const maxOrdinal =
        (await sequelize.models.Chapter.max("ordinalNumber", {
          where: { storyId },
          transaction,
        })) || 0;

      const chapter = await sequelize.models.Chapter.create(
        {
          ...chapterData,
          ordinalNumber: chapterData.ordinalNumber || maxOrdinal + 1,
          viewNum: 0,
          lockedStatus:
            chapterData.ordinalNumber === 1
              ? false
              : chapterData.lockedStatus ?? shouldLock,
          updatedAt: new Date(),
        },
        { transaction }
      );

      await sequelize.models.Story.increment("chapterNum", {
        where: { storyId },
        transaction,
      });

      return chapter;
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
    return chapter;
  },

  async readChapter(chapterId, userId) {
    return await handleTransaction(async (transaction) => {
      const chapter = await validateChapter(chapterId, true);
      if (chapter.lockedStatus && userId !== chapter.story.userId) {
        const canAccess = await canUserAccessChapter(userId, chapter);
        if (!canAccess) {
          throw new ApiError("Bạn cần mua chương này để đọc", 403);
        }
      }

      await Promise.all([
        sequelize.models.History.upsert(
          { userId, chapterId, lastReadAt: new Date() },
          { transaction }
        ),
        chapter.increment("viewNum", { transaction }),
        sequelize.models.Story.increment("viewNum", {
          where: { storyId: chapter.story.storyId },
          transaction,
        }),
      ]);

      return chapter;
    });
  },

  async updateChapter(chapterId, chapterData, userId) {
    const chapter = await validateChapter(chapterId, true);
    await validateStory(
      chapter.story.storyId,
      userId,
      "Bạn không có quyền sửa chương này"
    );
    return await chapter.update({ ...chapterData, updatedAt: new Date() });
  },

  async deleteChapter(chapterId, userId) {
    return await handleTransaction(async (transaction) => {
      const chapter = await validateChapter(chapterId, true);
      await validateStory(
        chapter.story.storyId,
        userId,
        "Bạn không có quyền xóa chương này"
      );

      await Promise.all([
        sequelize.models.Comment.destroy({ where: { chapterId }, transaction }),
        sequelize.models.Purchase.destroy({
          where: { chapterId },
          transaction,
        }),
        sequelize.models.History.destroy({ where: { chapterId }, transaction }),
        chapter.destroy({ transaction }),
        sequelize.models.Story.decrement("chapterNum", {
          where: { storyId: chapter.story.storyId },
          transaction,
        }),
      ]);

      const remainingChapters = await sequelize.models.Chapter.findAll({
        where: {
          storyId: chapter.story.storyId,
          ordinalNumber: { [sequelize.Sequelize.Op.gt]: chapter.ordinalNumber },
        },
        order: [["ordinalNumber", "ASC"]],
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
    orderBy = "ordinalNumber",
    sort = "ASC"
  ) {
    await validateStory(storyId);
    return await getChaptersByStory({
      storyId,
      userId,
      limit,
      lastId,
      orderBy,
      sort,
    });
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
