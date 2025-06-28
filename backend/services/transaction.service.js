import { sequelize } from '../models/index.js';
import ApiError from '../utils/api_error.util.js';
import { Op } from 'sequelize';

const Transaction = sequelize.models.Transaction;
const User = sequelize.models.User;

const TransactionService = {
  async createTransaction(transactionData) {
    try {
      const transaction = await Transaction.create({
        ...transactionData,
        status: 'pending',
        time: transactionData.time || new Date(),
        finishAt: null,
      });
      return transaction;
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi tạo giao dịch', 500);
    }
  },

  async getTransactionById(transactionId) {
    try {
      const transaction = await Transaction.findByPk(transactionId, {
        include: [
          {
            model: User,
            as: 'user',
            attributes: ['userId', 'userName', 'dUserName', 'avatarId'],
          },
        ],
      });

      if (!transaction) {
        throw new ApiError('Không tìm thấy giao dịch', 404);
      }

      return transaction;
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi lấy thông tin giao dịch', 500);
    }
  },

  async getUserTransactions(userId, { limit = 20, lastId = null } = {}) {
    try {
      const where = {
        userId,
        ...(lastId
          ? {
              transactionId: {
                [Op.lt]: lastId,
              },
            }
          : {}),
      };

      const transactions = await Transaction.findAll({
        where,
        limit: parseInt(limit),
        attributes: [
          'transactionId',
          'money',
          'type',
          'time',
          'status',
          'finishAt',
        ],
        include: [
          {
            model: User,
            as: 'user',
            attributes: ['userId', 'userName', 'dUserName', 'avatarId'],
          },
        ],
        time: [['time', 'ASC']],
        order: [['transactionId', 'DESC']],
      });

      const nextLastId =
        transactions.length > 0
          ? transactions[transactions.length - 1].transactionId
          : null;

      return {
        transactions,
        nextLastId,
        hasMore: transactions.length === parseInt(limit),
      };
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi lấy danh sách giao dịch', 500);
    }
  },

  async updateTransaction(transactionId, updateData) {
    try {
      const transaction = await Transaction.findByPk(transactionId);
      if (!transaction) {
        throw new ApiError('Không tìm thấy giao dịch', 404);
      }

      await transaction.update(updateData);
      return transaction;
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi cập nhật giao dịch', 500);
    }
  },

  async deleteTransaction(transactionId) {
    try {
      const transaction = await Transaction.findByPk(transactionId);
      if (!transaction) {
        throw new ApiError('Không tìm thấy giao dịch', 404);
      }

      await transaction.destroy();
      return { message: 'Xóa giao dịch thành công' };
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi xóa giao dịch', 500);
    }
  },
};

export default TransactionService;
