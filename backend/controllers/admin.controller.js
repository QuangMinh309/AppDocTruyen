import UserManagerService from '../services/admin/user_management.service.js';
import StoryManagerService from '../services/admin/story_management.service.js';
import TransactionManagerService from '../services/admin/transaction_accept.service.js';
import ReportService from '../services/admin/report_turnover.service.js';
import ApiError from '../utils/api_error.util.js';
import { sequelize } from '../models/index.js';

const AdminController = {
  // Quản lý user
  async getAllUsers(req, res, next) {
    try {
      const offset = parseInt(req.query.offset) || 0;
      const limit = 15;

      const result = await UserManagerService.getAllUsers(offset, limit);

      res.status(200).json({
        success: true,
        data: result,
      });
    } catch (error) {
      next(error);
    }
  },

  async lockUser(req, res, next) {
    try {
      const { userId } = req.params;

      const result = await UserManagerService.lockUser(parseInt(userId));

      res.status(200).json({
        success: true,
        message: result.message,
      });
    } catch (error) {
      next(error);
    }
  },

  async unlockUser(req, res, next) {
    try {
      const { userId } = req.params;

      const result = await UserManagerService.unlockUser(parseInt(userId));

      res.status(200).json({
        success: true,
        message: result.message,
      });
    } catch (error) {
      next(error);
    }
  },

  // Quản lý story
  async approveStory(req, res, next) {
    try {
      const storyId = req.params.storyId;
      const approvalData = req.body;

      const { story, result } = await StoryManagerService.approveStory(
        storyId,
        approvalData
      );

      const message =
        result === 'approved'
          ? 'Truyện đã được duyệt thành công.'
          : 'Từ chối public truyện thành công';

      res.status(200).json({
        success: true,
        data: story,
        message,
      });
    } catch (error) {
      next(error);
    }
  },

  // Quản lý Transaction
  async approveTransaction(req, res, next) {
    try {
      const transactionId = req.params.transactionId;
      const approvalData = req.body;

      const { story, result } =
        await TransactionManagerService.approveTransaction(
          transactionId,
          approvalData
        );

      const message =
        result === 'success'
          ? 'Giao dịch đã được xác thực.'
          : 'Từ chối cập nhật giao dịch thành công';

      res.status(200).json({
        success: true,
        message,
      });
    } catch (error) {
      next(error);
    }
  },

  // Lấy báo cáo
  async getDailyRevenueByMonth(req, res, next) {
    try {
      const { year, month } = req.query;

      const yearNum = parseInt(year);
      const monthNum = parseInt(month);

      // Gọi service để lấy báo cáo
      const dailyRevenues = await ReportService.getDailyRevenueByMonth(
        sequelize,
        yearNum,
        monthNum
      );

      res.status(200).json({
        success: true,
        data: dailyRevenues,
      });
    } catch (error) {
      next(error);
    }
  },
};

export default AdminController;
