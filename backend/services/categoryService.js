const { Category, Community, StoryCategory } = require('../models');
import ApiError from "./../utils/apiError";

class CategoryService {
  async createCategory(data) {
      try {
        const category = await Category.create(data);
        return category.toJSON();
      }
      catch(err) {
        throw new ApiError('Lỗi khi tạo thể loại', 500);
      }
  }

  async getCategoryById(categoryId) {
    try {
        const category = await Category.findByPk(categoryId, {
          include: [
            { model: Community, attributes: ['communityId', 'communitytName'] },
            { model: StoryCategory, attributes: ['storyId'] },
          ],
        });
        if (!category) throw new ApiError('Thể loại không tồn tại', 404);
        return category.toJSON();
    }
    catch(err) {
        throw new ApiError('Lỗi khi lấy thể loại', 500);
    }
  }

  async getAllCategories(limit = 10) {
    const categories = await Category.findAll({ limit });
    return categories.map(category => category.toJSON());
  }

  async updateCategory(categoryId, data) {
    const category = await Category.findByPk(categoryId);
    if (!category) throw new ApiError('Thể loại không tồn tại', 404);
    try {
        await category.update(data);
        return category.toJSON();
    }
    catch(err) {
        throw new ApiError('Lỗi khi cập nhật thể loại', 500);
    }
  }

  async deleteCategory(categoryId) {
    const category = await Category.findByPk(categoryId);
    if (!category) throw new ApiError('Thể loại không tồn tại', 404);
    try {
        await category.destroy();
        return { message: 'Thể loại xoá thành công' };
    }
    catch(err) {
        throw new ApiError('Lỗi khi cập nhật thể loại', 500);
    }
  }
}

module.exports = new CategoryService();