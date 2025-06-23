import { sequelize } from '../../models/index.js';
import ApiError from '../../utils/api_error.util.js';
import { publicStory } from '../../utils/story.util.js';
import { Op } from 'sequelize';
import { formatDate } from '../../utils/date.util.js';

const Story = sequelize.models.Story;
const User = sequelize.models.User;
const Category = sequelize.models.Category;

const getStoriesByUpdateDate = async ({
  limit = 20,
  lastId = null,
  role = 'user',
} = {}) => {
  try {
    const statusFilter = publicStory(role);

    const where = {
      ...statusFilter,
      ...(lastId && { storyId: { [Op.lt]: lastId } }),
    };

    const stories = await Story.findAll({
      where,
      limit: parseInt(limit),
      include: [
        {
          model: User,
          as: 'author',
          attributes: ['userId', 'userName', 'avatarId'],
        },
        {
          model: Category,
          as: 'categories',
          attributes: ['categoryId', 'categoryName'],
          through: { attributes: [] },
        },
      ],
      order: [
        ['updatedAt', 'DESC'],
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
      : new ApiError('Lỗi khi lọc truyện theo ngày cập nhật', 500);
  }
};

export default getStoriesByUpdateDate;
