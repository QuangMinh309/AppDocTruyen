import { sequelize } from '../models/index.js'
import ApiError from './api_error.util.js'

const Category = sequelize.models.Category

// Check category validate
export const validateCategory = async (categoryId) => {
  const category = await Category.findByPk(categoryId)
  if (!category) throw new ApiError('Thể loại không tồn tại', 404)
  return category
}
