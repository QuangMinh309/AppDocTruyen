import { sequelize } from '../../models/index.js';
import ApiError from '../../utils/api_error.util.js';
import { updateStoryViewNum } from '../../utils/story.util.js';
import { formatDate } from '../../utils/date.util.js';
import { checkChapterAccessCore } from '../../utils/chapter.util.js';
import { isPremiumUser } from '../../middlewares/auth.middleware.js';

const Story = sequelize.models.Story;
const Chapter = sequelize.models.Chapter;
const User = sequelize.models.User;
const Category = sequelize.models.Category;

const getStoryById = async (storyId, userId, isPremium = false) => {
  try {
    const story = await Story.findByPk(storyId, {
      include: [
        {
          model: User,
          as: 'author',
          attributes: ['userId', 'userName', 'avatarId', 'dUserName'],
        },
        {
          model: Chapter,
          as: 'chapters',
          attributes: [
            'chapterId',
            'chapterName',
            'ordinalNumber',
            'updatedAt',
            'lockedStatus',
          ],
          order: [['ordinalNumber', 'ASC']],
        },
        {
          model: Category,
          as: 'categories',
          attributes: ['categoryId', 'categoryName'],
          through: { attributes: [] },
        },
      ],
    });

    if (!story) throw new ApiError('Truyện không tồn tại', 404);

    story.dataValues.createdAt = formatDate(story.createdAt);
    story.dataValues.updatedAt = formatDate(story.updatedAt);

    // Cập nhật lượt xem
    await updateStoryViewNum(storyId);

    // Ẩn lockedStatus nếu người dùng có quyền truy cập
    if (userId && Array.isArray(story.chapters)) {
      for (const chapter of story.chapters) {
        const canAccess = await checkChapterAccessCore(
          userId,
          storyId,
          chapter.chapterId,
          false,
          isPremium
        );
        if (canAccess) {
          chapter.dataValues.lockedStatus = false;
        }
      }
    }

    return story;
  } catch (err) {
    throw err instanceof ApiError
      ? err
      : new ApiError('Lỗi khi lấy truyện', 500);
  }
};

export default getStoryById;
