import TransactionService from '../services/transaction.service.js';

export default {
  async createTransaction(req, res, next) {
    try {
      const userId = req.user.userId;
      const transactionData = { userId, ...req.body };
      const transaction = await TransactionService.createTransaction(transactionData);
      res.status(201).json(transaction);
    } catch (error) {
      next(error);
    }
  },

  async getTransaction(req, res, next) {
    try {
      const transaction = await TransactionService.getTransactionById(
        req.params.transactionId
      );
      res.status(200).json(transaction);
    } catch (error) {
      next(error);
    }
  },

  async getUserTransactions(req, res, next) {
    try {
      const { userId } = req.params;
      const { limit, lastId } = req.query;
      const result = await TransactionService.getUserTransactions(userId, {
        limit,
        lastId,
      });
      // console.log(result)
      res.status(200).json(result);
    } catch (error) {
      next(error);
    }
  },

  async updateTransaction(req, res, next) {
    try {
      const transaction = await TransactionService.updateTransaction(
        req.params.transactionId,
        req.body
      );
      res.status(200).json(transaction);
    } catch (error) {
      next(error);
    }
  },

  async deleteTransaction(req, res, next) {
    try {
      const result = await TransactionService.deleteTransaction(
        req.params.transactionId
      );
      res.status(200).json(result);
    } catch (error) {
      next(error);
    }
  },
};
