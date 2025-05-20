import express from "express";
import validate from "../middlewares/validate.js";
const router = express.Router();
import CategoryValidation from "../validators/categoryValidation.js";
import { CategoryController } from "../controllers/categoryController.js";

router.post('/', validate(CategoryValidation.create), CategoryController.createCategory);
router.get('/:categoryId', validate(CategoryValidation.idSchema), CategoryController.getCategoryById);
router.get('/', CategoryController.getAllCategories);
router.put('/:categoryId', validate(CategoryValidation.update), CategoryController.updateCategory);
router.delete('/:categoryId', validate(CategoryValidation.idSchema), CategoryController.deleteCategory);

export default router;