import Notification from '../models/entities/notification.js'
import ApiError from '../utils/api_error.util.js'

const NotificationService = {
  async createNotification(data) {
    try {
      const notification = await Notification.create(data)
      return notification.toJSON()
    } catch (err) {
      throw new ApiError('Lỗi khi tạo thông báo', 500)
    }
  },

  async getNotificationById(notificationId) {
    try {
      const notification = await Notification.findByPk(notificationId)
      if (!notification) throw new ApiError('Thông báo không tồn tại', 404)
      return notification.toJSON()
    } catch (err) {
      throw new ApiError('Lỗi khi lấy thông báo', 500)
    }
  },

  async getAllNotifications(limit = 10) {
    const notifications = await Notification.findAll({ limit })
    return notifications.map((notification) => notification.toJSON())
  },

  async updateNotification(notificationId, data) {
    const notification = await Notification.findByPk(notificationId)
    if (!notification) throw new ApiError('Thông báo không tồn tại', 404)
    try {
      await notification.update(data)
      return notification.toJSON()
    } catch (err) {
      throw new ApiError('Lỗi khi cập nhật thông báo', 500)
    }
  },

  async deleteNotification(notificationId) {
    const notification = await Notification.findByPk(notificationId)
    if (!notification) throw new ApiError('Thông báo không tồn tại', 404)
    try {
      await notification.destroy()
      return { message: 'Xoá thông báo thành công' }
    } catch (err) {
      throw err
    }
  },

  // Dùng để thông báo cho follower
  async notifyFollowers(userId, storyId, message) {
    try {
      const followers = await User.findAll({
        include: [
          {
            model: User,
            as: 'followers',
            through: { attributes: [] },
            attributes: ['userId'],
          },
        ],
        where: { userId },
      })

      const notifications = followers[0].followers.map((follower) => ({
        userId: follower.userId,
        storyId,
        message,
        createdAt: new Date(),
      }))

      await Notification.bulkCreate(notifications)
    } catch (err) {
      console.error('Error in notifyFollowers:', err)
      throw new ApiError('Lỗi khi gửi thông báo', 500)
    }
  },
}

export default NotificationService
