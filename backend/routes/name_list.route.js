import express from 'express';
import NameListController from '../controllers/name_list.controller.js';
import NameListValidation from './../validators/name_list.validation.js';
import validate from '../middlewares/validate.middleware.js';
import { canManageReadingList } from '../middlewares/permission.middleware.js';
import { authenticate } from '../middlewares/auth.middleware.js';

const router = express.Router();

router.use(authenticate);

router.post(
  '/',
  validate(NameListValidation.createReadingList),
  NameListController.createReadingList
);

router.put(
  '/:nameListId',
  validate(NameListValidation.updateReadingList),
  canManageReadingList,
  NameListController.updateReadingList
);

router.get(
  '/user',
  validate(NameListValidation.getUserReadingLists, 'query'),
  NameListController.getUserReadingLists
);

router.get(
  '/:nameListId',
  validate(NameListValidation.getReadingList, 'query'),
  NameListController.getReadingList
);

router.delete(
  '/:nameListId',
  canManageReadingList,
  NameListController.deleteReadingList
);

router.post(
  '/:nameListId/stories',
  validate(NameListValidation.addToReadingList),
  canManageReadingList,
  NameListController.addToReadingList
);

router.delete(
  '/:nameListId/stories/:storyId',
  canManageReadingList,
  NameListController.removeFromReadingList
);

export default router;
