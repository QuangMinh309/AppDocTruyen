import { models, sequelize } from '../models/index.js';
import { Op } from 'sequelize';
import ApiError from './api_error.util.js';
import NotificationService from '../services/notification.service.js';

const validateStory = async (
  storyId,
  userId = null,
  errorMessage = 'Truyện không tồn tại'
) => {
  const story = await models.Story.findByPk(storyId);
  if (!story) throw new ApiError(errorMessage, 404);
  if (
    userId &&
    story.userId !== userId &&
    story.user.role.roleName !== 'admin'
  ) {
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

// Xử lý truyện public
const publicStory = (roleName) => {
  if (roleName === 'admin') return {};
  return {
    status: {
      [Op.in]: ['update', 'full'],
    },
  };
};

export { validateStory, updateStoryViewNum, validateSortParams, publicStory };
