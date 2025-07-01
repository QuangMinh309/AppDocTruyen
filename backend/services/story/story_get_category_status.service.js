import { sequelize } from '../../models/index.js';
import ApiError from '../../utils/api_error.util.js';
import { validateSortParams } from '../../utils/story.util.js';
import { validateCategory } from '../../utils/category.util.js';
import { Op } from 'sequelize';
import { formatDate } from '../../utils/date.util.js';

const Story = sequelize.models.Story;
const User = sequelize.models.User;
const Category = sequelize.models.Category;
const StoryCategory = sequelize.models.StoryCategory;

const getStoriesByCategoryAndStatus = async (
  categoryId,
  status,
  { limit = 20, lastId = null, orderBy = 'createdAt', sort = 'DESC', role } = {}
) => {
  try {
    await validateCategory(categoryId);

    const allowedStatusesByRole = {
      user: ['update', 'full'],
      admin: ['pending', 'rejected', 'approved', 'update', 'full'],
    };

    const validStatuses = allowedStatusesByRole[role] || [];
    if (!validStatuses.includes(status)) {
      throw new ApiError(
        'Trạng thái truyện không hợp lệ với vai trò của bạn',
        403
      );
    }

    const validOrderFields = ['createdAt', 'updatedAt', 'voteNum', 'viewNum'];
    const { orderBy: finalOrderBy, sort: finalSort } = validateSortParams(
      orderBy,
      sort,
      validOrderFields
    );

    const storyCategoryIds = categoryId
      ? (
          await StoryCategory.findAll({
            attributes: ['storyId'],
            where: { categoryId },
          })
        ).map((item) => item.storyId)
      : null;

    const where = {
      ...(lastId && {
        storyId: {
          [finalSort === 'DESC' ? Op.lt : Op.gt]: lastId,
        },
      }),
      ...(storyCategoryIds && {
        storyId: {
          [Op.in]: storyCategoryIds,
        },
      }),
      status,
    };

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
      order: [[finalOrderBy, finalSort]],
      distinct: true,
    });

    const nextLastId =
      stories.length > 0 ? stories[stories.length - 1].storyId : null;

    const formattedStories = stories.map((story) => ({
      ...story.toJSON(),
      createdAt: formatDate(story.createdAt),
      updatedAt: formatDate(story.updatedAt),
    }));

    return {
      stories: formattedStories,
      nextLastId,
      hasMore: stories.length === parseInt(limit),
    };
  } catch (error) {
    throw error instanceof ApiError
      ? error
      : new ApiError('Lỗi khi lọc truyện theo thể loại và trạng thái', 500);
  }
};

export default getStoriesByCategoryAndStatus;
