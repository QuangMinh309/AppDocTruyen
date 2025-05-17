import { models } from "../models/index.js";
import ApiError from "./../utils/apiError";

const StoryService = {
  async createStory(storyData, userId) {
    const transaction = await models.sequelize.transaction();
    try {
      const story = await models.Story.create(
        {
          ...storyData,
          userId,
          viewNum: 0,
          voteNum: 0,
          chapterNum: 0,
        },
        { transaction }
      );
      await transaction.commit();
      return story;
    } catch (err) {
      await transaction.rollback();
      throw new ApiError("Lỗi khi tạo truyện", 500);
    }
  },

  async getStoryById(storyId) {
    try {
      const story = await models.Story.findByPk(storyId, {
        include: [
          {
            model: models.User,
            attributes: ["userId", "userName"],
          },
          {
            model: models.Chapter,
            attributes: ["chapterId", "chapterName", "ordinalNumber"],
          },
        ],
      });
      if (!story) {
        throw new ApiError("Truyện không tồn tại", 404);
      }
      return story;
    } catch (err) {
      throw new ApiError("Lỗi khi lấy truyện", 500);
    }
  },

  async updateStoryViewNum(storyId, transaction = null) {
    const total = await models.Chapter.sum("viewNum", {
      where: { storyId },
      transaction,
    });

    await models.Story.update(
      { viewNum: total },
      { where: { storyId }, transaction }
    );
  },

  async updateStory(storyId, storyData, userId) {
    const story = await models.Story.findByPk(storyId);
    if (!story) {
      throw new ApiError("Truyện không tồn tại", 404);
    }
    if (story.userId !== userId) {
      throw new ApiError("Bạn không có quyền sửa truyện này", 403);
    }
    try {
      await story.update(storyData);
      return story;
    } catch (err) {
      throw new ApiError("Lỗi khi lấy truyện", 500);
    }
  },

  async deleteStory(storyId, userId) {
    const story = await models.Story.findByPk(storyId);
    if (!story) {
      throw new ApiError("Truyện không tồn tại", 404);
    }
    if (story.userId !== userId) {
      throw new ApiError("Không có quyền xóa truyện này", 403);
    }
    const transaction = await models.sequelize.transaction();
    try {
      await models.Chapter.destroy({ where: { storyId }, transaction });
      await story.destroy({ transaction });
      await transaction.commit();
    } catch (error) {
      await transaction.rollback();
      throw error;
    }
  },
};

export default StoryService;
