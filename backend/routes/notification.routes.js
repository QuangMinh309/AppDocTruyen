import express from 'express'
import validate from '../middlewares/validate.middleware.js'
const router = express.Router()
import NotificationValidation from '../validators/notification.validation.js'
import NotificationController from '../controllers/notification.controller.js'

router.post(
  '/',
  validate(NotificationValidation.create),
  NotificationController.createNotification
)
router.get(
  '/:notificationId',
  validate(NotificationValidation.idSchema),
  NotificationController.getNotificationById
)
router.get(
  '/',
  validate(NotificationValidation.idSchema),
  NotificationController.getAllNotifications
)
router.put(
  '/:notificationId',
  validate(NotificationValidation.update),
  NotificationController.updateNotification
)
router.delete(
  '/:notificationId',
  validate(NotificationValidation.idSchema),
  NotificationController.deleteNotification
)

export default router
