import { models } from "../models/index.js";
import ApiError from "./../utils/apiError";

const ChapterService = {
  // Tạo chapter mới
  async createChapter(chapterData) {
    const { storyId } = chapterData;
    const story = await models.Story.findByPk(storyId);
    if (!story) {
      throw new ApiError("Truyện không tồn tại", 404);
    }
    if (story.userId !== userId) {
      throw new ApiError("Bạn không có quyền tạo chương cho truyện này", 403);
    }

    const transaction = await models.sequelize.transaction();
    try {
      const chapter = await models.Chapter.create(
        {
          ...chapterData,
          viewNum: 0,
        },
        { transaction }
      );
      await models.Story.increment("chapterNum", { transaction });
      await transaction.commit();
      return chapter;
    } catch (error) {
      await transaction.rollback();
      throw new ApiError("Lỗi khi tạo chương", 500);
    }
  },

  // Nhận chapter theo ID
  async getChapterById(chapterId) {
    try {
      const chapter = await models.Chapter.findByPk(chapterId, {
        include: [
          {
            model: models.Story,
            attributes: ["storyId", "storyName"],
          },
        ],
      });
      if (!chapter) {
        throw new ApiError("Chương không tồn tại", 404);
      }
      return chapter;
    } catch (error) {
      throw new ApiError("Lỗi khi lấy chương", 500);
    }
  },

  // Tăng số lượt xem khi đọc chapter
  async readChapter(chapterId) {
    const transaction = await models.sequelize.transaction();

    try {
      const chapter = await models.Chapter.findByPk(chapterId, {
        include: [
          {
            model: models.Story,
            attributes: ["storyId", "storyName", "lockedStatus"],
          },
        ],
        transaction,
      });

      if (!chapter) {
        throw new ApiError("Chương không tồn tại", 404);
      }
      await chapter.increment("viewNum", { transaction });
      await models.Story.increment("viewNum", {
        where: { storyId: chapter.storyId },
        transaction,
      });

      await transaction.commit();
      return chapter;
    } catch (error) {
      await transaction.rollback();
      throw new ApiError("Lỗi khi đọc chương", 500);
    }
  },

  // Cập nhật chapter
  async updateChapter(chapterId, chapterData, userId) {
    const chapter = await models.Chapter.findByPk(chapterId, {
      include: [{ model: models.Story }],
    });
    if (!chapter) {
      throw new ApiError("Chương không tồn tại", 404);
    }
    if (chapter.Story.userId !== userId) {
      throw new ApiError("Bạn không có quyền sửa chương này", 403);
    }

    try {
      await chapter.update(chapterData);
      return chapter;
    } catch (error) {
      throw new ApiError("Lỗi khi cập nhật chương", 500);
    }
  },

  // Xóa chapter
  async deleteChapter(chapterId, userId) {
    const chapter = await models.Chapter.findByPk(chapterId, {
      include: [{ model: models.Story }],
    });

    if (!chapter) {
      throw new ApiError("Chương không tồn tại", 404);
    }
    if (chapter.Story.userId !== userId) {
      throw new ApiError("Bạn không có quyền xóa chương này", 403);
    }

    const transaction = await models.sequelize.transaction();
    try {
      await chapter.destroy({ transaction });
      await models.Story.decrement("chapterNum", {
        where: { storyId: chapter.storyId },
        transaction,
      });
      await transaction.commit();
    } catch (error) {
      await transaction.rollback();
      throw new ApiError("Lỗi khi xóa chương", 500);
    }
  },
};

export default ChapterService;
