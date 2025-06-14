import express from 'express';
import validate from '../middlewares/validate.middleware.js';
import CategoryValidation from '../validators/category.validation.js';
import CategoryController from '../controllers/category.controller.js';
import {
  authenticate,
  authorizeRoles,
} from '../middlewares/auth.middleware.js';

const router = express.Router();

router.use(authenticate);

router.get('/', CategoryController.getAllCategories);

router.get(
  '/:categoryId',
  validate(CategoryValidation.idSchema, 'params'),
  CategoryController.getCategoryById
);

// Dành cho admin
router.post(
  '/',
  validate(CategoryValidation.create, 'body'),
  authorizeRoles('admin'),
  CategoryController.createCategory
);

router.put(
  '/:categoryId',
  validate(CategoryValidation.update, 'body'),
  authorizeRoles('admin'),
  CategoryController.updateCategory
);

router.delete(
  '/:categoryId',
  validate(CategoryValidation.idSchema, 'params'),
  authorizeRoles('admin'),
  CategoryController.deleteCategory
);

export default router;
