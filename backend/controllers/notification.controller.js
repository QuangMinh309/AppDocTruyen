import NotificationService from '../services/notification.service.js'
import notificationSchema from '../validators/notification.validation.js'

const NotificationController = {
  async createNotification(req, res, next) {
    try {
      const notification = await NotificationService.createNotification(
        req.body
      )
      res.status(201).json(notification)
    } catch (error) {
      next(error)
    }
  },

  async getNotificationById(req, res, next) {
    try {
      const notification = await NotificationService.getNotificationById(
        req.params.notificationId
      )
      res.status(200).json(notification)
    } catch (error) {
      next(error)
    }
  },

  async getAllNotifications(req, res, next) {
    try {
      const notifications = await NotificationService.getAllNotifications(
        req.query.limit
      )
      res.status(200).json(notifications)
    } catch (error) {
      next(error)
    }
  },

  async updateNotification(req, res, next) {
    try {
      const notification = await NotificationService.updateNotification(
        req.params.notificationId,
        req.body
      )
      res.status(200).json(notification)
    } catch (error) {
      next(error)
    }
  },

  async deleteNotification(req, res, next) {
    try {
      const result = await NotificationService.deleteNotification(
        req.params.notificationId
      )
      res.status(200).json(result)
    } catch (error) {
      next(error)
    }
  },
}

export default NotificationController
