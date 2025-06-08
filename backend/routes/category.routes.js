import express from 'express';
import validate from '../middlewares/validate.middleware.js';
const router = express.Router();
import CategoryValidation from '../validators/category.validation.js';
import CategoryController from '../controllers/category.controller.js';
import { authorizeRoles } from '../middlewares/auth.middleware.js';

router.post(
  '/',
  validate(CategoryValidation.create),
  authorizeRoles('admin'),
  CategoryController.createCategory
);
router.get(
  '/:categoryId',
  validate(CategoryValidation.idSchema),
  CategoryController.getCategoryById
);
router.get('/', CategoryController.getAllCategories);
router.put(
  '/:categoryId',
  validate(CategoryValidation.update),
  authorizeRoles('admin'),
  CategoryController.updateCategory
);
router.delete(
  '/:categoryId',
  validate(CategoryValidation.idSchema),
  authorizeRoles('admin'),
  CategoryController.deleteCategory
);

export default router;
