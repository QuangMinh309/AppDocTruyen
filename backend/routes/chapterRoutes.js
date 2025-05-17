import express from "express";
import validate from "../middlewares/validate.js";
import ChapterValidation from "../validators/chapterValidation.js";
import { ChapterController } from "../controllers/chapterController.js";

const router = express.Router();

router.post(
  "/",
  validate(ChapterValidation.chapterCreateSchema),
  ChapterController.create
);

router.get(
  "/:chapterId",
  validate(ChapterValidation.idSchema),
  ChapterController.get
);

router.get(
  "/:chapterId/read",
  validate(ChapterValidation.idSchema),
  ChapterController.read
);

router.put(
  "/:chapterId",
  validate(ChapterValidation.chapterUpdateSchema),
  ChapterController.update
);

router.delete(
  "/:chapterId",
  validate(ChapterValidation.idSchema),
  ChapterController.delete
);

export default router;
