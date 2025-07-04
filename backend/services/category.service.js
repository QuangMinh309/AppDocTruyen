import { sequelize } from '../models/index.js';
import ApiError from '../utils/api_error.util.js';
import { validateCategory } from '../utils/category.util.js';

const Category = sequelize.models.Category;
const Community = sequelize.models.Community;

const CategoryService = {
  async createCategory(data) {
    try {
      const existing = await Category.findOne({
        where: { categoryName: data.categoryName },
      });
      if (existing) {
        throw new ApiError('Tên thể loại đã tồn tại', 400);
      }

      const category = await Category.create(data);
      return category.toJSON();
    } catch (err) {
      if (err instanceof ApiError) throw err;
      throw new ApiError('Lỗi khi tạo thể loại', 500);
    }
  },

  async getCategoryById(categoryId) {
    try {
      const category = await Category.findByPk(categoryId);

      if (!category) throw new ApiError('Thể loại không tồn tại', 404);
      return category.toJSON();
    } catch (err) {
      console.error('Lỗi: ' + err);
      throw new ApiError('Lỗi khi lấy thể loại', 500);
    }
  },

  async getAllCategories(limit = 30) {
    const categories = await Category.findAll({ limit });
    return categories.map((category) => category.toJSON());
  },

  async updateCategory(categoryId, data) {
    const category = await validateCategory(categoryId);

    if (data.categoryName && data.categoryName !== category.categoryName) {
      const existing = await Category.findOne({
        where: { categoryName: data.categoryName },
      });
      if (existing) {
        throw new ApiError('Tên thể loại đã tồn tại', 400);
      }
    }

    try {
      await category.update(data);
      return category.toJSON();
    } catch (err) {
      throw new ApiError('Lỗi khi cập nhật thể loại', 500);
    }
  },

  async deleteCategory(categoryId) {
    const category = await Category.findByPk(categoryId);
    if (!category) throw new ApiError('Thể loại không tồn tại', 404);

    const associatedCommunities = await category.countCommunities();
    if (associatedCommunities > 0) {
      throw new ApiError(
        'Không thể xóa thể loại vì đã có cộng đồng liên kết',
        400
      );
    }

    const associatedStories = await category.countStories();
    if (associatedStories > 0) {
      throw new ApiError(
        'Không thể xóa thể loại vì đã có câu chuyện liên kết',
        400
      );
    }

    try {
      await category.destroy();
      return { message: 'Thể loại xóa thành công' };
    } catch (err) {
      throw new ApiError('Lỗi khi xóa thể loại', 500);
    }
  },
};

export default CategoryService;
