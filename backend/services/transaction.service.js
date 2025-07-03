
import { sequelize } from '../models/index.js';
import ApiError from '../utils/api_error.util.js';
import { Op } from 'sequelize';

const Transaction = sequelize.models.Transaction;
const User = sequelize.models.User;

const TransactionService = {
  async createTransaction(transactionData) {
    try {
      const user = await User.findByPk(transactionData.userId)
      if (!user)
        throw new ApiError("người dùng không tồn tại!", 404)

      if (transactionData.type == "withdraw" && user.wallet - transactionData.money < 0)
        throw new ApiError("Tiền trong ví không đủ để rút!", 400)

      const transaction = await Transaction.create({
        ...transactionData,
        status: transactionData.status || 'pending',
        time: transactionData.time || new Date(),
        finishAt: transactionData.finishAt,
      });
      return transaction;
    } catch (error) {
      // console.log(error)
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

  async getUserTransactions(userId, { limit = 20, lastId =null } = {}) {
    try {
      console.log('getUserTransactions', userId, limit, lastId)
      if (!userId) {
        throw new ApiError('Thiếu thông tin người dùng', 400);
      }
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
          ? transactions[0].transactionId
          : null;
      console.log(transactions)
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
      if (updateData.status == "success")
        updateData.finishAt = new Date()

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
