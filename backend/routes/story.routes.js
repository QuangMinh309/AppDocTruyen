import express from 'express';
import StoryController from '../controllers/story.controller.js';
import { authenticate } from '../middlewares/auth.middleware.js';
import { isStoryAuthor } from '../middlewares/permission.middleware.js';
import {
  validateCreateStory,
  validateStoryId,
  validateGetStories,
  validateGetByCategory,
  validateFilterByCategoryAndStatus,
  validateSearchStories,
  validateFilterByUser,
  validatePurchaseChapter,
} from '../validators/story.validation.js';
import validate from '../middlewares/validate.middleware.js';
import { uploadSingleImage } from '../middlewares/uploadImage.middleware.js'; // Import middleware mới

const router = express.Router();

router.use(authenticate);

router.get('/', validate(validateGetStories), StoryController.getAllStories);

router.get(
  '/category/:categoryId',
  validate(validateGetByCategory),
  StoryController.getByCategory
);

router.get(
  '/category/:categoryId/status/:status',
  validate(validateFilterByCategoryAndStatus),
  StoryController.getStoriesByCategoryAndStatus
);

router.get(
  '/vote',
  validate(validateGetStories),
  StoryController.getStoriesByVote
);

router.get(
  '/updated',
  validate(validateGetStories),
  StoryController.getStoriesByUpdateDate
);

router.get(
  '/search',
  validate(validateSearchStories),
  StoryController.searchStories
);

router.get(
  '/user/:userId',
  validate(validateFilterByUser),
  StoryController.getStoriesByUser
);

router.get(
  '/:storyId',
  validate(validateStoryId),
  StoryController.getStoryById
);

router.post(
  '/',
  uploadSingleImage('coverImgId'),
  validate(validateCreateStory),
  StoryController.createStory
);

router.put('/:storyId', isStoryAuthor, StoryController.updateStory);

router.delete(
  '/:storyId',
  validate(validateStoryId),
  isStoryAuthor,
  StoryController.deleteStory
);

router.post(
  '/:storyId/vote',
  validate(validateStoryId),
  StoryController.toggleVote
);

router.get(
  '/:storyId/vote/status',
  validate(validateStoryId),
  StoryController.checkVoteStatus
);

router.post(
  '/:storyId/purchase',
  validate(validateStoryId),
  StoryController.purchaseEntireStory
);

router.get(
  '/:storyId/chapters/:chapterId/purchase',
  validate(validatePurchaseChapter),
  StoryController.checkChapterPurchase
);

router.get(
  '/recently-read',
  validate(validateGetStories),
  StoryController.getRecentlyReadStories
);

router.get(
  '/purchased',
  validate(validateGetStories),
  StoryController.getPurchasedStories
);

export default router;
