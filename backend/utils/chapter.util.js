import { models, sequelize } from '../models/index.js';
import ApiError from './api_error.util.js';
import NotificationService from '../services/notification.service.js';

export const validateChapter = async (chapterId, includeStory = false) => {
  try {
    const include = includeStory
      ? [
          {
            model: models.Story,
            as: 'story',
            attributes: ['storyId', 'storyName', 'userId'],
          },
        ]
      : [];
    const chapter = await models.Chapter.findByPk(chapterId, { include });
    if (!chapter) throw new ApiError('Chương không tồn tại', 404);
    return chapter;
  } catch (error) {
    throw error instanceof ApiError
      ? error
      : new ApiError('Lỗi khi kiểm tra chương', 500);
  }
};

export const checkChapterAccessCore = async (
  userId,
  storyId,
  chapterId,
  returnDetails = false,
  isPremium = false
) => {
  try {
    const chapter = await models.Chapter.findByPk(chapterId, {
      include: [{ model: models.Story, as: 'story' }],
    });
    if (!chapter) throw new ApiError('Chương không tồn tại', 404);

    if (chapter.storyId !== parseInt(storyId)) {
      throw new ApiError('Chương không thuộc truyện này', 400);
    }

    if (!chapter.lockedStatus) {
      return returnDetails ? { purchased: true, isUnlocked: true } : true;
    }

    if (chapter.story.userId === userId) {
      return returnDetails ? { purchased: true, isAuthor: true } : true;
    }

    if (isPremium) {
      return returnDetails ? { purchased: true, isPremium: true } : true;
    }

    const parameters = await models.Parameter.findOne();
    if (!parameters) {
      throw new ApiError('Không tìm thấy tham số hệ thống', 500);
    }

    const chapterPurchase = await models.Purchase.findOne({
      where: { userId, chapterId },
      attributes: ['purchasedId', 'userId', 'chapterId', 'purchasedAt'],
    });

    let isStillValid = false;
    if (chapterPurchase) {
      const expiryDate = new Date(
        chapterPurchase.purchasedAt.getTime() +
          parameters.Chapter_Access_Duration * 24 * 60 * 60 * 1000
      );
      isStillValid = expiryDate >= new Date();
    }

    return returnDetails ? { purchased: isStillValid } : isStillValid;
  } catch (error) {
    throw error instanceof ApiError
      ? error
      : new ApiError('Lỗi khi kiểm tra quyền truy cập chương', 500);
  }
};

export const canUserAccessChapter = async (
  userId,
  chapter,
  isPremium = false
) => {
  return await checkChapterAccessCore(
    userId,
    chapter.storyId,
    chapter.chapterId,
    false,
    isPremium
  );
};

// Hàm phụ trợ để chuẩn hóa tham số sắp xếp
export const validateSortParams = (orderBy, sort, validFields) => {
  const finalOrderBy = validFields.includes(orderBy) ? orderBy : validFields[0];
  const finalSort = ['ASC', 'DESC'].includes(sort) ? sort : 'ASC';
  return { orderBy: finalOrderBy, sort: finalSort };
};
