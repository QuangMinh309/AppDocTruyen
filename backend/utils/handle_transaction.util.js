import { sequelize } from '../models/index.js';
import ApiError from './api_error.util.js';

export const handleTransaction = async (callback) => {
  const transaction = await sequelize.transaction();
  try {
    const result = await callback(transaction);
    await transaction.commit();
    return result;
  } catch (error) {
    await transaction.rollback();
    console.error('Lỗi: ' + error);
    throw error instanceof ApiError
      ? error
      : new ApiError('Đã xảy ra lỗi trong quá trình xử lý', 500);
  }
};
