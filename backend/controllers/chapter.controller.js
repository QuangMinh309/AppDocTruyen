import ChapterService from '../services/chapter/chapter.service.js';
import ChapterReadingService from '../services/chapter/chapter_read.service.js';

const ChapterController = {
  async createChapter(req, res, next) {
    try {
      const userId = req.user.userId;
      const { storyId } = req.params;
      const chapterData = { ...req.body, storyId };
      const chapter = await ChapterService.createChapter(chapterData, userId);
      return res.status(201).json({ success: true, data: chapter });
    } catch (error) {
      return next(error);
    }
  },

  async readChapter(req, res, next) {
    try {
      const { chapterId } = req.params;
      const userId = req.user ? req.user.userId : null;
      const chapter = await ChapterReadingService.readChapter(
        chapterId,
        userId,
        req.user.isPremium
      );
      return res.status(200).json({ success: true, data: chapter });
    } catch (error) {
      return next(error);
    }
  },

  async readNextChapter(req, res, next) {
    try {
      const { chapterId } = req.params;
      const userId = req.user ? req.user.userId : null;
      const chapter = await ChapterReadingService.readNextChapter(
        chapterId,
        userId,
        req.user.isPremium
      );
      return res.status(200).json({ success: true, data: chapter });
    } catch (error) {
      return next(error);
    }
  },

  async getChapterById(req, res, next) {
    try {
      const { chapterId } = req.params;
      const userId = req.user ? req.user.userId : null;
      const chapter = await ChapterService.getChapterById(chapterId, userId);
      return res.status(200).json({ success: true, data: chapter });
    } catch (error) {
      return next(error);
    }
  },

  async getChaptersByStory(req, res, next) {
    try {
      const { storyId } = req.params;
      const userId = req.user ? req.user.userId : null;
      const { limit, lastId, orderBy, sort } = req.query;
      const result = await ChapterService.getChaptersByStory(
        storyId,
        userId,
        limit,
        lastId,
        orderBy,
        sort
      );
      return res.status(200).json({ success: true, data: result });
    } catch (error) {
      return next(error);
    }
  },

  async updateChapter(req, res, next) {
    try {
      const { chapterId } = req.params;
      const userId = req.user.userId;
      const chapterData = req.body;
      const updatedChapter = await ChapterService.updateChapter(
        chapterId,
        chapterData,
        userId
      );
      return res.status(200).json({ success: true, data: updatedChapter });
    } catch (error) {
      return next(error);
    }
  },

  async deleteChapter(req, res, next) {
    try {
      const { chapterId } = req.params;
      const userId = req.user.userId;
      await ChapterService.deleteChapter(chapterId, userId);
      return res
        .status(200)
        .json({ success: true, message: 'Xóa chương thành công' });
    } catch (error) {
      return next(error);
    }
  },
};

export default ChapterController;
