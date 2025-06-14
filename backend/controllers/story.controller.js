import createStory from '../services/story/story_create.service.js';
import updateStory from '../services/story/story_update.service.js';
import deleteStory from '../services/story/story_delete.service.js';
import getStoriesByCategory from '../services/story/story_get_category.service.js';
import getStoryById from '../services/story/story_get_id.service.js';
import getAllStories from '../services/story/story_get_all.service.js';
import getStoriesByCategoryAndStatus from '../services/story/story_get_category_status.service.js';
import getStoriesByVote from '../services/story/story_get_vote.service.js';
import getStoriesByUpdateDate from '../services/story/story_get_update_date.service.js';
import getStoriesByUser from '../services/story/story_get_user.service.js';
import searchStories from '../services/story/story_search.service.js';

import StoryPurchaseService from '../services/story/story_interact.service.js';

const StoryController = {
  async createStory(req, res, next) {
    try {
      const storyData = req.body;
      const userId = req.user.userId;
      const file = req.file;

      // Parse categories nếu là chuỗi JSON
      if (storyData.categories) {
        if (typeof storyData.categories === 'string') {
          try {
            storyData.categories = JSON.parse(storyData.categories);
          } catch (err) {
            storyData.categories = storyData.categories
              ? [Number(storyData.categories.replace(/\[|\]/g, ''))] // Xử lý chuỗi như "[1]"
              : [];
          }
        }
        // Đảm bảo categories là mảng
        if (!Array.isArray(storyData.categories)) {
          storyData.categories = [Number(storyData.categories)];
        }
      } else {
        storyData.categories = [];
      }

      const story = await createStory(storyData, userId, file);
      res.status(201).json({
        success: true,
        data: story,
        message: 'Tạo truyện thành công, vui lòng chờ admin duyệt truyện',
      });
    } catch (error) {
      next(error);
    }
  },

  async getStoryById(req, res, next) {
    try {
      const { storyId } = req.params;
      const story = await getStoryById(storyId);
      return res.status(200).json({ success: true, data: story });
    } catch (error) {
      return next(error);
    }
  },

  async updateStory(req, res, next) {
    try {
      const { storyId } = req.params;
      const userId = req.user.userId;
      const storyData = req.body;
      const updatedStory = await updateStory(storyId, storyData, userId);
      return res.status(200).json({ success: true, data: updatedStory });
    } catch (error) {
      return next(error);
    }
  },

  async deleteStory(req, res, next) {
    try {
      const { storyId } = req.params;
      const userId = req.user.userId;
      const result = await deleteStory(storyId, userId);
      return res.status(200).json({ success: true, message: result.message });
    } catch (error) {
      return next(error);
    }
  },

  async getAllStories(req, res, next) {
    try {
      const { limit, lastId, orderBy, sort } = req.query;
      const result = await getAllStories({
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

  async getByCategory(req, res, next) {
    try {
      const { categoryId } = req.params;
      const { limit, lastId, orderBy, sort } = req.query;
      const result = await getStoriesByCategory(categoryId, {
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

  async getStoriesByCategoryAndStatus(req, res, next) {
    try {
      const { categoryId, status } = req.params;
      const { limit, lastId, orderBy, sort } = req.query;
      const result = await getStoriesByCategoryAndStatus(categoryId, status, {
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

  async getStoriesByVote(req, res, next) {
    try {
      const { limit, lastId } = req.query;
      const result = await getStoriesByVote({ limit, lastId });
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },

  async getStoriesByUpdateDate(req, res, next) {
    try {
      const { limit, lastId } = req.query;
      const result = await getStoriesByUpdateDate({ limit, lastId });
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },

  async getStoriesByUser(req, res, next) {
    try {
      const { userId } = req.params;
      const { limit, lastId, includeAll } = req.query;
      const result = await getStoriesByUser(userId, {
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
      const { searchTerm, limit, lastId } = req.query;
      const result = await searchStories(searchTerm, {
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
      const userId = req.user.userId;
      const { limit, lastId } = req.query;
      const result = await StoryPurchaseService.getRecentlyReadStories(userId, {
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
      const userId = req.user.userId;
      const { limit, lastId } = req.query;
      const result = await StoryPurchaseService.getPurchasedStories(userId, {
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
      const userId = req.user.userId;
      const { storyId } = req.params;
      const result = await StoryPurchaseService.toggleVote(userId, storyId);
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },

  async checkVoteStatus(req, res, next) {
    try {
      const userId = req.user.userId;
      const { storyId } = req.params;
      const result = await StoryPurchaseService.checkVoteStatus(
        userId,
        storyId
      );
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },

  async purchaseEntireStory(req, res, next) {
    try {
      const userId = req.user.userId;
      const { storyId } = req.params;
      const result = await StoryPurchaseService.purchaseEntireStory(
        userId,
        storyId
      );
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },

  async checkChapterPurchase(req, res, next) {
    try {
      const userId = req.user.userId;
      const { storyId, chapterId } = req.params;
      const result = await StoryPurchaseService.checkChapterPurchase(
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
