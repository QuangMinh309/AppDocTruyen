import { sequelize } from '../../models/index.js'
import ApiError from '../../utils/api_error.util.js'
import { Op } from 'sequelize'

const Story = sequelize.models.Story
const User = sequelize.models.User
const Category = sequelize.models.Category

const searchStories = async (
  searchTerm,
  { limit = 20, lastId = null } = {}
) => {
  try {
    const where = {
      storyName: { [Op.like]: `%${searchTerm}%` },
      ...(lastId ? { storyId: { [Op.lt]: lastId } } : {}),
    }

    const stories = await Story.findAll({
      where,
      limit: parseInt(limit),
      include: [
        {
          model: User,
          as: 'author',
          attributes: ['userId', 'userName', 'avatarId', 'dUserName'],
        },
        {
          model: Category,
          as: 'categories',
          attributes: ['categoryId', 'categoryName'],
          through: { attributes: [] },
        },
      ],
      order: [
        ['createdAt', 'DESC'],
        ['storyId', 'DESC'],
      ],
    })

    const nextLastId =
      stories.length > 0 ? stories[stories.length - 1].storyId : null
    return {
      stories,
      nextLastId,
      hasMore: stories.length === parseInt(limit),
    }
  } catch (error) {
    throw error instanceof ApiError
      ? error
      : new ApiError('Lỗi khi tìm kiếm truyện', 500)
  }
}

export default searchStories
