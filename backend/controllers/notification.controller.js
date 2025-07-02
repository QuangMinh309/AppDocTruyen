import NotificationService from '../services/notification.service.js';

const NotificationController = {
  async createNotification(req, res, next) {
    try {
      const notification = await NotificationService.createNotification(
        req.body
      );
      res.status(201).json(notification);
    } catch (error) {
      next(error);
    }
  },

  async getNotificationById(req, res, next) {
    try {
      const notification = await NotificationService.getNotificationById(
        req.params.notificationId
      );
      res.status(200).json(notification);
    } catch (error) {
      next(error);
    }
  },

  async getAllNotifications(req, res, next) {
    try {
      const notifications = await NotificationService.getAllNotifications(
        req.query.limit
      );
      res.status(200).json(notifications);
    } catch (error) {
      next(error);
    }
  },

  async getUserNotifications(req, res, next) {
    try {
      const userId = req.user.userId;
      const limit = req.query.limit || 10;

      const notifications = await NotificationService.getNotificationsByUserId(
        userId,
        parseInt(limit)
      );

      res.status(200).json({
        success: true,
        data: notifications,
      });
    } catch (error) {
      next(error);
    }
  },

  async getUnreadCount(req, res, next) {
    try {
      const userId = req.user.userId;
      const count = await NotificationService.getUnreadCount(userId);

      res.status(200).json({
        success: true,
        data: count,
      });
    } catch (error) {
      next(error);
    }
  },

  async updateNotification(req, res, next) {
    try {
      const notification = await NotificationService.updateNotification(
        req.params.notificationId,
        req.body
      );
      res.status(200).json(notification);
    } catch (error) {
      next(error);
    }
  },

  async deleteNotification(req, res, next) {
    try {
      const result = await NotificationService.deleteNotification(
        req.params.notificationId
      );
      res.status(200).json({ message: 'Xóa thông báo thành công' });
    } catch (error) {
      next(error);
    }
  },
};

export default NotificationController;
