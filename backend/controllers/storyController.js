import StoryService from "../services/storyService.js";

const StoryController = {
  async create(req, res, next) {
    try {
      const story = await StoryService.createStory(req.body, req.user.userId);
      res.status(201).json(story);
    } catch (error) {
      next(error);
    }
  },

  async get(req, res, next) {
    try {
      const story = await StoryService.getStoryById(req.params.storyId);
      res.status(200).json(story);
    } catch (error) {
      next(error);
    }
  },

  async update(req, res, next) {
    try {
      const { storyId, ...storyData } = req.body;
      const updated = await StoryService.updateStory(
        storyId,
        storyData,
        req.user.userId
      );
      res.status(200).json(updated);
    } catch (error) {
      next(error);
    }
  },

  async delete(req, res, next) {
    try {
      await StoryService.deleteStory(req.params.storyId, req.user.userId);
      res.status(204).send();
    } catch (error) {
      next(error);
    }
  },

  async updateViewNum(req, res, next) {
    try {
      await StoryService.updateStoryViewNum(req.params.storyId);
      res.status(200).json({ message: "Cập nhật lượt xem thành công" });
    } catch (error) {
      next(error);
    }
  },
};

export default StoryController;
