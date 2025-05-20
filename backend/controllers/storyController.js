import { validationResult } from "express-validator";
import StoryService from "../services/storyService.js";
import ApiError from "../utils/apiError.js";

const StoryController = {
  async createStory(req, res, next) {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return next(
          new ApiError("Dữ liệu đầu vào không hợp lệ", 400, errors.array())
        );
      }

      const userId = req.user.userId;
      const storyData = req.body;
      const story = await StoryService.createStory(storyData, userId);
      return res.status(201).json({ success: true, data: story });
    } catch (error) {
      return next(error);
    }
  },

  async getStoryById(req, res, next) {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return next(
          new ApiError("Dữ liệu đầu vào không hợp lệ", 400, errors.array())
        );
      }

      const { storyId } = req.params;
      const story = await StoryService.getStoryById(storyId);
      return res.status(200).json({ success: true, data: story });
    } catch (error) {
      return next(error);
    }
  },

  async updateStory(req, res, next) {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return next(
          new ApiError("Dữ liệu đầu vào không hợp lệ", 400, errors.array())
        );
      }

      const { storyId } = req.params;
      const userId = req.user.userId;
      const storyData = req.body;
      const updatedStory = await StoryService.updateStory(
        storyId,
        storyData,
        userId
      );
      return res.status(200).json({ success: true, data: updatedStory });
    } catch (error) {
      return next(error);
    }
  },

  async deleteStory(req, res, next) {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return next(
          new ApiError("Dữ liệu đầu vào không hợp lệ", 400, errors.array())
        );
      }

      const { storyId } = req.params;
      const userId = req.user.userId;
      const result = await StoryService.deleteStory(storyId, userId);
      return res.status(200).json({ success: true, message: result.message });
    } catch (error) {
      return next(error);
    }
  },

  async getAllStories(req, res, next) {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return next(
          new ApiError("Dữ liệu đầu vào không hợp lệ", 400, errors.array())
        );
      }

      const { limit, lastId, orderBy, sort } = req.query;
      const result = await StoryService.getAllStories({
        limit,
        lastId,
        orderBy,
        sort,
      });
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },

  async filterByCategory(req, res, next) {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return next(
          new ApiError("Dữ liệu đầu vào không hợp lệ", 400, errors.array())
        );
      }

      const { categoryId } = req.params;
      const { limit, lastId, orderBy, sort } = req.query;
      const result = await StoryService.filterByCategory(categoryId, {
        limit,
        lastId,
        orderBy,
        sort,
      });
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },

  async filterByCategoryAndStatus(req, res, next) {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return next(
          new ApiError("Dữ liệu đầu vào không hợp lệ", 400, errors.array())
        );
      }

      const { categoryId, status } = req.params;
      const { limit, lastId, orderBy, sort } = req.query;
      const result = await StoryService.filterByCategoryAndStatus(
        categoryId,
        status,
        { limit, lastId, orderBy, sort }
      );
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },

  async filterByVote(req, res, next) {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return next(
          new ApiError("Dữ liệu đầu vào không hợp lệ", 400, errors.array())
        );
      }

      const { limit, lastId } = req.query;
      const result = await StoryService.filterByVote({ limit, lastId });
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },

  async filterByUpdateDate(req, res, next) {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return next(
          new ApiError("Dữ liệu đầu vào không hợp lệ", 400, errors.array())
        );
      }

      const { limit, lastId } = req.query;
      const result = await StoryService.filterByUpdateDate({ limit, lastId });
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },

  async filterByUser(req, res, next) {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return next(
          new ApiError("Dữ liệu đầu vào không hợp lệ", 400, errors.array())
        );
      }

      const { userId } = req.params;
      const { limit, lastId, includeAll } = req.query;
      const result = await StoryService.filterByUser(userId, {
        limit,
        lastId,
        includeAll,
      });
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },

  async searchStories(req, res, next) {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return next(
          new ApiError("Dữ liệu đầu vào không hợp lệ", 400, errors.array())
        );
      }

      const { searchTerm, limit, lastId } = req.query;
      const result = await StoryService.searchStories(searchTerm, {
        limit,
        lastId,
      });
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },

  async getRecentlyReadStories(req, res, next) {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return next(
          new ApiError("Dữ liệu đầu vào không hợp lệ", 400, errors.array())
        );
      }

      const userId = req.user.userId;
      const { limit, lastId } = req.query;
      const result = await StoryService.getRecentlyReadStories(userId, {
        limit,
        lastId,
      });
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },

  async getPurchasedStories(req, res, next) {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return next(
          new ApiError("Dữ liệu đầu vào không hợp lệ", 400, errors.array())
        );
      }

      const userId = req.user.userId;
      const { limit, lastId } = req.query;
      const result = await StoryService.getPurchasedStories(userId, {
        limit,
        lastId,
      });
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },

  // Chức năng vote
  async toggleVote(req, res, next) {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return next(
          new ApiError("Dữ liệu đầu vào không hợp lệ", 400, errors.array())
        );
      }

      const userId = req.user.userId;
      const { storyId } = req.params;
      const result = await StoryService.toggleVote(userId, storyId);
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },

  async checkVoteStatus(req, res, next) {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return next(
          new ApiError("Dữ liệu đầu vào không hợp lệ", 400, errors.array())
        );
      }

      const userId = req.user.userId;
      const { storyId } = req.params;
      const result = await StoryService.checkVoteStatus(userId, storyId);
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },

  async purchaseEntireStory(req, res, next) {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return next(
          new ApiError("Dữ liệu đầu vào không hợp lệ", 400, errors.array())
        );
      }

      const userId = req.user.userId;
      const { storyId } = req.params;
      const result = await StoryService.purchaseEntireStory(userId, storyId);
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },

  async checkChapterPurchase(req, res, next) {
    try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return next(
          new ApiError("Dữ liệu đầu vào không hợp lệ", 400, errors.array())
        );
      }

      const userId = req.user.userId;
      const { storyId, chapterId } = req.params;
      const result = await StoryService.checkChapterPurchase(
        userId,
        storyId,
        chapterId
      );
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },
};

export default StoryController;
