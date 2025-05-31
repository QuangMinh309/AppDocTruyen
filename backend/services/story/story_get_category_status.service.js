import { sequelize } from '../../models/index.js'
import ApiError from '../../utils/api_error.util.js'
import { validateSortParams } from '../../utils/story.util.js'
import { validateCategory } from '../../utils/category.util.js'
import { Op } from 'sequelize'

const Story = sequelize.models.Story
const User = sequelize.models.User
const Category = sequelize.models.Category

const getStoriesByCategoryAndStatus = async (
  categoryId,
  status,
  { limit = 20, lastId = null, orderBy = 'createdAt', sort = 'DESC' } = {}
) => {
  try {
    await validateCategory(categoryId)
    const validStatuses = ['update', 'full']
    if (!validStatuses.includes(status)) {
      throw new ApiError('Trạng thái truyện không hợp lệ', 400)
    }

    const validOrderFields = ['createdAt', 'updatedAt', 'voteNum', 'viewNum']
    const { orderBy: finalOrderBy, sort: finalSort } = validateSortParams(
      orderBy,
      sort,
      validOrderFields
    )

    const where = {
      status,
      ...(lastId
        ? {
            storyId: {
              [finalSort === 'DESC' ? Op.lt : Op.gt]: lastId,
            },
          }
        : {}),
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
          where: { categoryId },
          attributes: ['categoryId', 'categoryName'],
          through: { attributes: [] },
        },
      ],
      order: [[finalOrderBy, finalSort]],
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
      : new ApiError('Lỗi khi lọc truyện theo thể loại và trạng thái', 500)
  }
}

export default getStoriesByCategoryAndStatus
