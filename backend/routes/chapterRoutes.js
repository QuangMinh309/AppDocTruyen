import express from "express";
import ChapterController from "../controllers/chapterController.js";
import { authenticateJWT } from "../middlewares/authMiddleware.js";
import {
  isStoryAuthor,
  canAccessChapter,
} from "../middlewares/permissionMiddleware.js";
import {
  validateCreateChapter,
  validateUpdateChapter,
  validateChapterId,
  validateGetStories,
  validatePurchaseChapter,
} from "../validators/chapterValidation.js";
import validate from "../middlewares/validate.js"

const router = express.Router();

router.use(authenticateJWT);
router.post(
  "/story/:storyId",
  validate(validateCreateChapter),
  isStoryAuthor,
  ChapterController.createChapter
);
router.put(
  "/story/:storyId/:chapterId",
  validate(validateUpdateChapter),
  isStoryAuthor,
  ChapterController.updateChapter
);
router.delete(
  "/:chapterId",
  validate(validateChapterId),
  isStoryAuthor,
  ChapterController.deleteChapter
);
router.get(
  "/:chapterId/read",
  validate(validateChapterId),
  canAccessChapter,
  ChapterController.readChapter
);
router.post(
  "/story/:storyId/:chapterId/purchase",
  validate(validatePurchaseChapter),
  ChapterController.purchaseChapter
);

export default router;
