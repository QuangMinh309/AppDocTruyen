import { models, sequelize } from '../models/index.js';
import ApiError from './api_error.util.js';
import NotificationService from '../services/notification.service.js';

const validateStory = async (
  storyId,
  userId = null,
  errorMessage = 'Truyện không tồn tại'
) => {
  const story = await models.Story.findByPk(storyId);
  if (!story) throw new ApiError(errorMessage, 404);
  if (userId && story.userId !== userId) {
    throw new ApiError('Bạn không có quyền thực hiện hành động này', 403);
  }
  return story;
};

// Cập nhật số lượt xem của truyện
const updateStoryViewNum = async (storyId, transaction = null) => {
  try {
    const total = await models.Chapter.sum('viewNum', {
      where: { storyId },
      transaction,
    });

    await models.Story.update(
      { viewNum: total || 0 },
      { where: { storyId }, transaction }
    );

    return total || 0;
  } catch (err) {
    throw new ApiError('Lỗi khi cập nhật số lượt xem', 500);
  }
};

// Xác thực và chuẩn hóa các tham số sắp xếp
const validateSortParams = (orderBy, sort, validFields) => {
  const finalOrderBy = validFields.includes(orderBy) ? orderBy : validFields[0];
  const finalSort = ['ASC', 'DESC'].includes(sort) ? sort : 'ASC';
  return { orderBy: finalOrderBy, sort: finalSort };
};

// Xử lý giao dịch mua truyện
const handleStoryPurchaseTransaction = async (userId, storyId, transaction) => {
  try {
    const user = await models.User.findByPk(userId, { transaction });
    if (!user) throw new ApiError('Người dùng không tồn tại', 404);

    const story = await models.Story.findByPk(storyId, { transaction });
    if (!story) throw new ApiError('Truyện không tồn tại', 404);

    if (story.userId === userId) {
      throw new ApiError('Bạn là tác giả, không cần mua truyện', 400);
    }

    if (!story.price || story.price <= 0) {
      throw new ApiError('Truyện này không có giá bán', 400);
    }

    const existingPurchase = await models.Purchase.findOne({
      where: { userId, storyId, chapterId: null },
      transaction,
    });
    if (existingPurchase) {
      throw new ApiError('Bạn đã mua truyện này rồi', 400);
    }

    if (user.wallet < story.price) {
      throw new ApiError('Số dư không đủ', 400);
    }

    await models.Purchase.create(
      {
        userId,
        storyId,
        chapterId: null,
        purchasedAt: new Date(),
      },
      { transaction }
    );

    await user.update(
      { wallet: sequelize.literal(`wallet - ${story.price}`) },
      { transaction }
    );

    await models.Transaction.create(
      {
        userId,
        money: story.price,
        type: 'STORY_PURCHASE',
        time: new Date(),
        status: 'COMPLETED',
        finishAt: new Date(),
      },
      { transaction }
    );

    const author = await models.User.findByPk(story.userId, { transaction });
    const authorPayment = story.price * 0.8;

    await author.update(
      { wallet: sequelize.literal(`wallet + ${authorPayment}`) },
      { transaction }
    );

    await models.Transaction.create(
      {
        userId: author.userId,
        money: authorPayment,
        type: 'STORY_SALE',
        time: new Date(),
        status: 'COMPLETED',
        finishAt: new Date(),
      },
      { transaction }
    );

    await NotificationService.createNotification({
      type: 'SALE',
      content: `Truyện "${
        story.storyName
      }" đã được bán với giá ${authorPayment.toFixed(2)}`,
      refId: story.storyId,
      userId: story.userId,
      transaction,
    });

    return {
      success: true,
      message: 'Mua truyện thành công',
      purchasedStoryId: story.storyId,
    };
  } catch (error) {
    throw error instanceof ApiError
      ? error
      : new ApiError('Lỗi khi mua truyện', 500);
  }
};

export {
  validateStory,
  updateStoryViewNum,
  validateSortParams,
  handleStoryPurchaseTransaction,
};
