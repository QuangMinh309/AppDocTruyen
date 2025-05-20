import express from "express";
import StoryController from "../controllers/storyController.js";
import { authenticate } from "../middleware/authenticate.js"; // Giả sử bạn có middleware authenticate
import { isStoryAuthor } from "../middleware/auth.js";
import {
  validateCreateStory,
  validateUpdateStory,
  validateStoryId,
  validateGetStories,
  validateFilterByCategory,
  validateFilterByCategoryAndStatus,
  validateSearchStories,
  validateFilterByUser,
  validatePurchaseChapter,
} from "../validations/storyValidation.js";

const router = express.Router();

// Không yêu cầu đăng nhập
router.get("/", validateGetStories, StoryController.getAllStories);
router.get(
  "/category/:categoryId",
  validateFilterByCategory,
  StoryController.filterByCategory
);
router.get(
  "/category/:categoryId/status/:status",
  validateFilterByCategoryAndStatus,
  StoryController.filterByCategoryAndStatus
);
router.get("/vote", validateGetStories, StoryController.filterByVote);
router.get("/updated", validateGetStories, StoryController.filterByUpdateDate);
router.get("/search", validateSearchStories, StoryController.searchStories);
router.get("/user/:userId", validateFilterByUser, StoryController.filterByUser);
router.get("/:storyId", validateStoryId, StoryController.getStoryById);

// Yêu cầu đăng nhập
router.use(authenticate);
router.post("/", validateCreateStory, StoryController.createStory);
router.put(
  "/:storyId",
  validateUpdateStory,
  isStoryAuthor,
  StoryController.updateStory
);
router.delete(
  "/:storyId",
  validateStoryId,
  isStoryAuthor,
  StoryController.deleteStory
);
router.post("/:storyId/vote", validateStoryId, StoryController.toggleVote);
router.get(
  "/:storyId/vote/status",
  validateStoryId,
  StoryController.checkVoteStatus
);
router.post(
  "/:storyId/purchase",
  validateStoryId,
  StoryController.purchaseEntireStory
);
router.get(
  "/:storyId/chapters/:chapterId/purchase",
  validatePurchaseChapter,
  StoryController.checkChapterPurchase
);
router.get(
  "/recently-read",
  validateGetStories,
  StoryController.getRecentlyReadStories
);
router.get(
  "/purchased",
  validateGetStories,
  StoryController.getPurchasedStories
);

export default router;
