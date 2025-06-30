import { sequelize } from '../../models/index.js';
import ApiError from '../../utils/api_error.util.js';
import { validateUser } from '../../utils/user.util.js';
import { Op } from 'sequelize';
import { formatDate } from '../../utils/date.util.js';

const History = sequelize.models.History;
const Story = sequelize.models.Story;
const User = sequelize.models.User;
const Category = sequelize.models.Category;

const getRecentlyReadStories = async (
  userId,
  { limit = 20, lastId = null } = {}
) => {
  try {
    await validateUser(userId);

    const whereClause = { userId };
    if (lastId) {
      whereClause.historyId = { [Op.lt]: lastId };
    }

    const histories = await History.findAll({
      attributes: ['historyId', 'userId', 'storyId', 'lastReadAt'],
      where: whereClause,
      include: [
        {
          model: Story,
          as: 'story',
          attributes: [
            'storyId',
            'storyName',
            'coverImgId',
            'status',
            'createdAt',
            'updatedAt',
          ],
          required: true,
          include: [
            {
              model: User,
              as: 'author',
              attributes: ['userId', 'userName', 'avatarId', 'dUserName'],
              where: { userId },
            },
            {
              model: Category,
              as: 'categories',
              attributes: ['categoryId', 'categoryName'],
              through: { attributes: [] },
            },
          ],
        },
      ],
      order: [
        ['lastReadAt', 'DESC'],
        ['historyId', 'DESC'],
      ],
      limit: parseInt(limit),
    });

    const uniqueStories = [];
    const seenStoryIds = new Set();

    for (const history of histories) {
      const story = history.story;
      if (!story) continue;

      const storyId = story.storyId;
      if (!seenStoryIds.has(storyId)) {
        seenStoryIds.add(storyId);

        const storyData = story.toJSON();
        uniqueStories.push({
          ...storyData,
          createdAt: formatDate(storyData.createdAt),
          updatedAt: formatDate(storyData.updatedAt),
          lastReadAt: formatDate(history.lastReadAt),
        });
      }
    }

    const nextLastId =
      histories.length > 0 ? histories[histories.length - 1].historyId : null;

    return {
      stories: uniqueStories,
      nextLastId,
      hasMore: histories.length === parseInt(limit),
    };
  } catch (error) {
    console.error('Lỗi: ' + error);
    throw error instanceof ApiError
      ? error
      : new ApiError('Lỗi khi lấy truyện đã đọc gần đây', 500);
  }
};

export default getRecentlyReadStories;
