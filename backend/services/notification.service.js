import { sequelize } from '../models/index.js';
import ApiError from '../utils/api_error.util.js';
import { notifyUser } from '../utils/notify_user.util.js';

const Notification = sequelize.models.Notification;
const User = sequelize.models.User;

const NotificationService = {
  async createNotification(type, content, refId = 0, userId, transaction) {
    try {
      await Notification.create(
        {
          type,
          content,
          refId,
          status: 'UNREAD',
          createAt: new Date(),
          userId,
        },
        { transaction }
      );
    } catch (error) {
      // console.log(error)
      throw new ApiError('Lỗi khi tạo thông báo', 500);
    }
  },

  async getNotificationById(notificationId) {
    try {
      const notification = await Notification.findByPk(notificationId);
      if (!notification) throw new ApiError('Thông báo không tồn tại', 404);

      await this.markAsRead(notificationId);

      return notification.toJSON();
    } catch (err) {
      throw new ApiError('Lỗi khi lấy thông báo', 500);
    }
  },

  async getAllNotifications() {
    const notifications = await Notification.findAll({});
    return notifications.map((notification) => notification.toJSON());
  },

  async getNotificationsByUserId(userId) {
    try {
      const notifications = await Notification.findAll({
        where: { userId },
        order: [['createAt', 'DESC']],
      });
      
      return  Promise.all(notifications.map(async (notification) =>{
        await notification.update({status:"READ"})
        notification.status = "READ"
        return notification.toJSON();
      }));
      
    } catch (error) {
      throw new ApiError('Lỗi khi lấy thông báo của user', 500);
    }
  },

  async markAsRead(notificationId) {
    try {
      const notification = await Notification.findByPk(notificationId);
      if (!notification) throw new ApiError('Thông báo không tồn tại', 404);

      await notification.update({ status: 'READ' });
      return notification.toJSON();
    } catch (error) {
      throw new ApiError('Lỗi khi đánh dấu đã đọc', 500);
    }
  },

  async getUnreadCount(userId) {
    try {
      const count = await Notification.count({
        where: {
          userId,
          status: 'UNREAD',
        },
      });
      return count;
    } catch (error) {
      throw new ApiError('Lỗi khi đếm thông báo chưa đọc', 500);
    }
  },

  async updateNotification(notificationId, data) {
    const notification = await Notification.findByPk(notificationId);
    if (!notification) throw new ApiError('Thông báo không tồn tại', 404);
    try {
      await notification.update(data);
      return notification.toJSON();
    } catch (err) {
      throw new ApiError('Lỗi khi cập nhật thông báo', 500);
    }
  },

  async deleteNotification(notificationId) {
    const notification = await Notification.findByPk(notificationId);
    if (!notification) throw new ApiError('Thông báo không tồn tại', 404);
    try {
      await notification.destroy();
      return { message: 'Xoá thông báo thành công' };
    } catch (err) {
      throw err;
    }
  },

  // Dùng để thông báo cho follower
  async notifyFollowers(userId, storyId, message, transaction) {
    try {
      const user = await User.findByPk(userId, {
        include: [
          {
            model: User,
            as: 'followers',
            through: { attributes: [] },
            attributes: ['userId'],
          },
        ],
        transaction,
      });

      if (user && user.followers && user.followers.length > 0) {
        const notifications = user.followers.map((follower) => ({
          type: 'NEW_STORY_CHAPTER',
          content: message,
          refId: storyId,
          status: 'UNREAD',
          createAt: new Date(),
          userId: follower.userId,
        }));

        await Notification.bulkCreate(notifications, { transaction });
        const followersId = user.followers.map((user)=> user.userId)
        notifyUser(followersId)
      }
    } catch (err) {
      throw new ApiError('Lỗi khi gửi thông báo', 500);
    }
  },
};

export default NotificationService;
