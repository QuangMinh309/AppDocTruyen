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
import { uploadSingleImage } from '../middlewares/uploadImage.middleware.js';

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
  '/recently-read',
  validate(validateGetStories),
  StoryController.getRecentlyReadStories
);

router.get(
  '/purchased',
  validate(validateGetStories),
  StoryController.getPurchasedStories
);

router.get(
  '/:storyId',
  validate(validateStoryId),
  StoryController.getStoryById
);

router.get(
  '/:storyId/vote/status',
  validate(validateStoryId),
  StoryController.checkVoteStatus
);

router.get(
  '/:storyId/chapters/:chapterId/purchase',
  validate(validatePurchaseChapter),
  StoryController.checkChapterPurchase
);

router.post(
  '/',
  uploadSingleImage('coverImgId'),
  validate(validateCreateStory),
  StoryController.createStory
);

router.post(
  '/:storyId/vote',
  validate(validateStoryId),
  StoryController.toggleVote
);

// Dành cho tác giả
router.put(
  '/:storyId',
  isStoryAuthor,
  uploadSingleImage('coverImgId'),
  StoryController.updateStory
);

router.delete(
  '/:storyId',
  validate(validateStoryId),
  isStoryAuthor,
  StoryController.deleteStory
);

export default router;
