import { sequelize } from '../../models/index.js';
import ApiError from '../../utils/api_error.util.js';
import { validateSortParams } from '../../utils/story.util.js';
import { validateCategory } from '../../utils/category.util.js';
import { Op } from 'sequelize';
import { formatDate } from '../../utils/date.util.js';

const Story = sequelize.models.Story;
const User = sequelize.models.User;
const Category = sequelize.models.Category;

const getStoriesByCategory = async (
  categoryId,
  { limit = 20, lastId = null, orderBy = 'createdAt', sort = 'DESC' } = {}
) => {
  try {
    await validateCategory(categoryId);

    const validOrderFields = ['createdAt', 'updatedAt', 'voteNum', 'viewNum'];
    const { orderBy: finalOrderBy, sort: finalSort } = validateSortParams(
      orderBy,
      sort,
      validOrderFields
    );

    const where = lastId
      ? {
          storyId: {
            [finalSort === 'DESC' ? Op.lt : Op.gt]: lastId,
          },
        }
      : {};

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
    });

    const nextLastId =
      stories.length > 0 ? stories[stories.length - 1].storyId : null;

    const formattedStories = stories.map((story) => ({
      ...story.toJSON(),
      createdAt: formatDate(story.createdAt),
      updatedAt: formatDate(story.updatedAt),
    }));

    return {
      formattedStories,
      nextLastId,
      hasMore: stories.length === parseInt(limit),
    };
  } catch (error) {
    throw error instanceof ApiError
      ? error
      : new ApiError('Lỗi khi lọc truyện theo thể loại', 500);
  }
};

export default getStoriesByCategory;
