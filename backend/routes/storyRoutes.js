import express from "express";
import StoryController from "../controllers/storyController.js";
import { authenticateJWT } from "../middlewares/authMiddleware.js";
import { isStoryAuthor } from "../middlewares/permissionMiddleware.js";
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
} from "../validators/storyValidation.js";
import validate from "../middlewares/validate.js";

const router = express.Router();

router.use(authenticateJWT);

router.get("/", validate(validateGetStories), StoryController.getAllStories);
router.get(
  "/category/:categoryId",
  validate(validateFilterByCategory),
  StoryController.filterByCategory
);
router.get(
  "/category/:categoryId/status/:status",
  validate(validateFilterByCategoryAndStatus),
  StoryController.filterByCategoryAndStatus
);
router.get("/vote", validate(validateGetStories), StoryController.filterByVote);
router.get("/updated", validate(validateGetStories), StoryController.filterByUpdateDate);
router.get("/search", validate(validateSearchStories), StoryController.searchStories);
router.get("/user/:userId", validate(validateFilterByUser), StoryController.filterByUser);
router.get("/:storyId", validate(validateStoryId), StoryController.getStoryById);
router.post("/", validate(validateCreateStory), StoryController.createStory);
router.put(
  "/:storyId",
  validate(validateUpdateStory),
  isStoryAuthor,
  StoryController.updateStory
);
router.delete(
  "/:storyId",
  validate(validateStoryId),
  isStoryAuthor,
  StoryController.deleteStory
);
router.post("/:storyId/vote", validate(validateStoryId), StoryController.toggleVote);
router.get(
  "/:storyId/vote/status",
  validate(validateStoryId),
  StoryController.checkVoteStatus
);
router.post(
  "/:storyId/purchase",
  validate(validateStoryId),
  StoryController.purchaseEntireStory
);
router.get(
  "/:storyId/chapters/:chapterId/purchase",
  validate(validatePurchaseChapter),
  StoryController.checkChapterPurchase
);
router.get(
  "/recently-read",
  validate(validateGetStories),
  StoryController.getRecentlyReadStories
);
router.get(
  "/purchased",
  validate(validateGetStories),
  StoryController.getPurchasedStories
);

export default router;
