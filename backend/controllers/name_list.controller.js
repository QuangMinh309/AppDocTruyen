import NameListService from '../services/name_list.service.js';

const NameListController = {
  async createReadingList(req, res, next) {
    try {
      const { userId } = req.user;
      const result = await NameListService.createReadingList(userId, req.body);
      res.status(201).json(result);
    } catch (error) {
      next(error);
    }
  },

  async updateReadingList(req, res, next) {
    try {
      const { userId } = req.user;
      const { nameListId } = req.params;
      const result = await NameListService.updateReadingList(
        userId,
        Number(nameListId),
        req.body
      );
      res.status(200).json(result);
    } catch (error) {
      next(error);
    }
  },

  async getReadingList(req, res, next) {
    try {
      const { nameListId } = req.params;
      const { limit, lastId } = req.query;
      const result = await NameListService.getReadingList(Number(nameListId), {
        limit,
        lastId,
      });
      res.status(200).json(result);
    } catch (error) {
      next(error);
    }
  },

  async getUserReadingLists(req, res, next) {
    try {
      const { userId } = req.user;
      const { limit, lastId } = req.query;
      const result = await NameListService.getUserReadingLists(userId, {
        limit,
        lastId,
      });
      res.status(200).json(result);
    } catch (error) {
      next(error);
    }
  },

  async addToReadingList(req, res, next) {
    try {
      const { userId } = req.user;
      const { nameListId } = req.params;
      const { storyId } = req.body;
      const result = await NameListService.addToReadingList(
        userId,
        storyId,
        Number(nameListId)
      );
      res.status(201).json(result);
    } catch (error) {
      next(error);
    }
  },

  async removeFromReadingList(req, res, next) {
    try {
      const { userId } = req.user;
      const { nameListId, storyId } = req.params;
      const result = await NameListService.removeFromReadingList(
        userId,
        Number(storyId),
        Number(nameListId)
      );
      res.status(200).json(result);
    } catch (error) {
      next(error);
    }
  },

  async deleteReadingList(req, res, next) {
    try {
      const { userId } = req.user;
      const { nameListId } = req.params;
      const result = await NameListService.deleteReadingList(
        userId,
        Number(nameListId)
      );
      res.status(200).json({ message: 'Xóa danh sách đọc thành công' });
    } catch (error) {
      next(error);
    }
  },
};

export default NameListController;
