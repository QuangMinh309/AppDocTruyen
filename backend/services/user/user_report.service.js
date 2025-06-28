import { sequelize } from '../../models/index.js';
import { handleTransaction } from '../../utils/handle_transaction.util.js';
import ApiError from '../../utils/api_error.util.js';
import NotificationService from '../notification.service.js';

const User = sequelize.models.User;
const Role = sequelize.models.Role;

const reportUser = async (reason, reporterId, reportedUserId) => {
  return await handleTransaction(async (transaction) => {
    if (!reason) {
      throw new ApiError('Lý do tố cáo là bắt buộc', 400);
    }

    const reportedUser = await User.findByPk(reportedUserId, {
      attributes: ['userId', 'userName'],
      transaction,
    });

    if (!reportedUser) {
      throw new ApiError('Người dùng bị tố cáo không tồn tại', 404);
    }

    const adminRole = await Role.findOne({
      where: { roleName: 'admin' },
      transaction,
    });

    if (!adminRole) {
      throw new ApiError('Không tìm thấy vai trò admin', 500);
    }

    const admins = await User.findAll({
      where: { roleId: adminRole.roleId },
      attributes: ['userId'],
      transaction,
    });

    if (admins.length === 0) {
      throw new ApiError('Không có admin nào để nhận thông báo', 500);
    }

    const reporter = await User.findByPk(reporterId, {
      attributes: ['userName'],
      transaction,
    });

    if (reporterId === reportedUserId) {
      throw new ApiError('Không thể tố cáo chính mình', 400);
    }

    const adminNotifications = admins.map((admin) =>
      NotificationService.createNotification(
        'USER_REPORT',
        `Báo cáo từ ${reporter?.userName || 'Unknown'} về người dùng ${
          reportedUser.userName
        } với lý do: ${reason}`,
        reportedUserId,
        admin.userId,
        transaction
      )
    );

    await Promise.all(adminNotifications);

    return {
      message: 'Báo cáo đã được gửi đến admin',
      reportedUserId,
      reason: reason,
    };
  });
};

export default reportUser;
