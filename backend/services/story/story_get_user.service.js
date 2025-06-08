import { sequelize } from '../../models/index.js'
import ApiError from '../../utils/api_error.util.js'
import { validateUser } from '../../utils/user.util.js'
import { Op } from 'sequelize'

const Story = sequelize.models.Story
const User = sequelize.models.User

const getStoriesByUser = async (
  userId,
  { limit = 20, lastId = null, includeAll = false } = {}
) => {
  try {
    await validateUser(userId)
    const where = {
      userId,
      ...(lastId ? { storyId: { [Op.lt]: lastId } } : {}),
    }

    const stories = await Story.findAll({
      where,
      limit: parseInt(limit),
      include: [
        {
          model: User,
          as: 'author',
          attributes: ['userId', 'userName', 'dUserName'],
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
      : new ApiError('Lỗi khi lọc truyện theo người dùng', 500)
  }
}

export default getStoriesByUser
