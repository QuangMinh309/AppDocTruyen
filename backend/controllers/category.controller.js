import CategoryService from '../services/category.service.js';
import categorySchema from '../validators/category.validation.js';

const CategoryController = {
  async createCategory(req, res, next) {
    try {
      const category = await CategoryService.createCategory(req.body);
      res.status(201).json(category);
    } catch (error) {
      next(error);
    }
  },

  async getCategoryById(req, res, next) {
    try {
      const category = await CategoryService.getCategoryById(
        req.params.categoryId
      );
      res.status(200).json(category);
    } catch (error) {
      next(error);
    }
  },

  async getAllCategories(req, res, next) {
    try {
      const categories = await CategoryService.getAllCategories(
        req.query.limit
      );
      res.status(200).json(categories);
    } catch (error) {
      next(error);
    }
  },

  async updateCategory(req, res, next) {
    try {
      const category = await CategoryService.updateCategory(
        req.params.categoryId,
        req.body
      );
      res.status(200).json(category);
    } catch (error) {
      next(error);
    }
  },

  async deleteCategory(req, res, next) {
    try {
      const result = await CategoryService.deleteCategory(
        req.params.categoryId
      );
      res.status(200).json(result);
    } catch (error) {
      next(error);
    }
  },
};

export default CategoryController;
