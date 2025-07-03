import { sequelize } from '../../models/index.js';
import ApiError from '../../utils/api_error.util.js';
import { validateUser } from '../../utils/user.util.js';
import { Op } from 'sequelize';
import { formatDate } from '../../utils/date.util.js';

const Story = sequelize.models.Story;
const User = sequelize.models.User;
const Category = sequelize.models.Category;

const getStoriesByUser = async (
  targetUserId,
  { limit = 80, lastId = null, includeAll = false, role, currentUserId } = {}
) => {
  try {
    await validateUser(targetUserId);

    const isOwnerOrAdmin =
      Number(targetUserId) === Number(currentUserId) || role === 'admin';

    const where = {
      [Op.and]: [
        { userId: targetUserId },
        ...(isOwnerOrAdmin
          ? []
          : [
              {
                status: { [Op.in]: ['update', 'full'] },
              },
            ]),
        ...(lastId ? [{ storyId: { [Op.lt]: lastId } }] : []),
      ],
    };

    const stories = await Story.findAll({
      where,
      limit: parseInt(limit),
      include: [
        {
          model: User,
          as: 'author',
          attributes: ['userId', 'userName', 'dUserName'],
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
    });

    stories.forEach((story) => {
      story.dataValues.createdAt = formatDate(story.createdAt);
      story.dataValues.updatedAt = formatDate(story.updatedAt);
    });

    const nextLastId =
      stories.length > 0 ? stories[stories.length - 1].storyId : null;
    return {
      stories,
      nextLastId,
      hasMore: stories.length === parseInt(limit),
    };
  } catch (error) {
    throw error instanceof ApiError
      ? error
      : new ApiError('Lỗi khi lọc truyện theo người dùng', 500);
  }
};

export default getStoriesByUser;
