import { models } from '../../models/index.js';
import ApiError from '../../utils/api_error.util.js';
import { validateUser } from '../../utils/user.util.js';
import { formatDate } from '../../utils/date.util.js';

const getPurchasedStories = async (
  userId,
  { limit = 20, lastId = null } = {}
) => {
  try {
    await validateUser(userId);
    const where = {
      userId,
      chapterId: null, // Chỉ lấy purchase của story, không phải chapter
      ...(lastId ? { storyId: { [models.Sequelize.Op.lt]: lastId } } : {}),
    };

    const purchases = await models.Purchase.findAll({
      where,
      limit: parseInt(limit),
      attributes: ['storyId', 'purchasedAt'],
      include: [
        {
          model: models.Story,
          as: 'story',
          attributes: ['storyId', 'storyName', 'coverImgId', 'status'],
          include: [
            {
              model: models.User,
              as: 'author',
              attributes: ['userId', 'userName', 'avatarId', 'dUserName'],
            },
            {
              model: models.Category,
              as: 'categories',
              attributes: ['categoryId', 'categoryName'],
              through: { attributes: [] },
            },
          ],
        },
      ],
      order: [
        ['purchasedAt', 'DESC'],
        ['storyId', 'DESC'],
      ],
    });

    const stories = purchases.map((p) => {
      const storyData = p.story.toJSON();
      return {
        ...storyData,
        createdAt: formatDate(storyData.createdAt),
        updatedAt: formatDate(storyData.updatedAt),
        purchasedAt: p.purchasedAt,
      };
    });

    const nextLastId =
      purchases.length > 0 ? purchases[purchases.length - 1].storyId : null;

    return {
      stories,
      nextLastId,
      hasMore: purchases.length === parseInt(limit),
    };
  } catch (error) {
    throw error instanceof ApiError
      ? error
      : new ApiError('Lỗi khi lấy truyện đã mua', 500);
  }
};

export default getPurchasedStories;
