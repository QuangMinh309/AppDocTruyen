import { sequelize } from '../models/index.js'
import ApiError from './api_error.util.js'

export const handleTransaction = async (callback) => {
  const transaction = await sequelize.transaction()
  try {
    const result = await callback(transaction)
    await transaction.commit()
    return result
  } catch (error) {
    await transaction.rollback()
    throw error instanceof ApiError ? error : new ApiError('Lỗi giao dịch', 500)
  }
}
