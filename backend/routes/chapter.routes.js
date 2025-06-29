import express from 'express';
import ChapterController from '../controllers/chapter.controller.js';
import { authenticate } from '../middlewares/auth.middleware.js';
import {
  validateCreateChapter,
  validateUpdateChapter,
  validateChapterId,
  validatePurchaseChapter,
  validateStoryIdParam,
} from '../validators/chapter.validation.js';
import validate from '../middlewares/validate.middleware.js';
import {
  isStoryAuthor,
  canAccessChapter,
} from '../middlewares/permission.middleware.js';

const router = express.Router();

router.use(authenticate);

router.post(
  '/story/:storyId',
  validate(validateCreateChapter, 'body'),
  validate(validateStoryIdParam, 'params'),
  isStoryAuthor,
  ChapterController.createChapter
);

router.get(
  '/:chapterId/read',
  validate(validateChapterId, 'params'),
  canAccessChapter,
  ChapterController.readChapter
);

router.get(
  '/:chapterId/read-next',
  validate(validateChapterId, 'params'),
  canAccessChapter,
  ChapterController.readNextChapter
);

router.get(
  '/:storyId/chapters',
  validate(validateStoryIdParam, 'params'),
  ChapterController.getChaptersByStory
);

router.get(
  '/:chapterId',
  validate(validateChapterId, 'params'),
  canAccessChapter,
  ChapterController.getChapterById
);

router.put(
  '/story/:storyId/:chapterId',
  validate(validateUpdateChapter, 'body'),
  validate(validateChapterId, 'params'),
  isStoryAuthor,
  ChapterController.updateChapter
);

router.delete(
  '/:chapterId',
  validate(validateChapterId, 'params'),
  isStoryAuthor,
  ChapterController.deleteChapter
);

router.post(
  '/story/:storyId/:chapterId/purchase',
  validate(validatePurchaseChapter),
  ChapterController.purchaseChapter
);

export default router;
