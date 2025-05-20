import express from "express";
import ChapterController from "../controllers/chapterController.js";
import { authenticate } from "../middleware/authenticate.js"; // Giả sử bạn có middleware authenticate
import { isStoryAuthor, canAccessChapter } from "../middleware/auth.js";
import {
  validateCreateChapter,
  validateUpdateChapter,
  validateChapterId,
  validateGetStories,
  validatePurchaseChapter,
} from "../validations/storyValidation.js";

const router = express.Router();

// Không yêu cầu đăng nhập
router.get("/:chapterId", validateChapterId, ChapterController.getChapterById);
router.get(
  "/story/:storyId",
  validateGetStories,
  ChapterController.getChaptersByStory
);

// Yêu cầu đăng nhập
router.use(authenticate);
router.post(
  "/story/:storyId",
  validateCreateChapter,
  isStoryAuthor,
  ChapterController.createChapter
);
router.put(
  "/story/:storyId/:chapterId",
  validateUpdateChapter,
  isStoryAuthor,
  ChapterController.updateChapter
);
router.delete(
  "/:chapterId",
  validateChapterId,
  isStoryAuthor,
  ChapterController.deleteChapter
);
router.get(
  "/:chapterId/read",
  validateChapterId,
  canAccessChapter,
  ChapterController.readChapter
);
router.post(
  "/story/:storyId/:chapterId/purchase",
  validatePurchaseChapter,
  ChapterController.purchaseChapter
);

export default router;
