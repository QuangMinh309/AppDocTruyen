import ChapterService from "../services/chapterService";

const ChapterController = {
  async create(req, res, next) {
    try {
      const chapter = await ChapterService.createChapter({
        ...req.body,
        userId: req.user.id,
      });
      res.status(201).json({ status: 201, data: chapter });
    } catch (err) {
      next(err);
    }
  },

  async get(req, res, next) {
    try {
      const chapter = await ChapterService.getChapterById(req.params.chapterId);
      res.status(200).json({ status: 200, data: chapter });
    } catch (err) {
      next(err);
    }
  },

  async read(req, res, next) {
    try {
      const chapter = await ChapterService.readChapter(req.params.chapterId);
      res.status(200).json({ status: 200, data: chapter });
    } catch (err) {
      next(err);
    }
  },

  async update(req, res, next) {
    try {
      const chapter = await ChapterService.updateChapter(
        req.params.chapterId,
        req.body,
        req.user.id
      );
      res.status(200).json({ status: 200, data: chapter });
    } catch (err) {
      next(err);
    }
  },

  async delete(req, res, next) {
    try {
      await ChapterService.deleteChapter(req.params.chapterId, req.user.id);
      res.status(200).json({ status: 200, message: "Xóa chương thành công" });
    } catch (err) {
      next(err);
    }
  },
};

export default ChapterController;
