import express from 'express';
import validate from '../middlewares/validate.middleware.js';
import NotificationValidation from '../validators/notification.validation.js';
import NotificationController from '../controllers/notification.controller.js';
import { authenticate } from '../middlewares/auth.middleware.js';

const router = express.Router();

router.use(authenticate);

router.post(
  '/',
  validate(NotificationValidation.create),
  NotificationController.createNotification
);

router.get('/my-notifications', NotificationController.getUserNotifications);

router.get('/unread-count', NotificationController.getUnreadCount);

router.get(
  '/',
  validate(NotificationValidation.idSchema),
  NotificationController.getAllNotifications
);

router.get(
  '/:notificationId',
  validate(NotificationValidation.idSchema),
  NotificationController.getNotificationById
);

router.put(
  '/:notificationId',
  validate(NotificationValidation.update),
  NotificationController.updateNotification
);

router.delete(
  '/:notificationId',
  validate(NotificationValidation.idSchema),
  NotificationController.deleteNotification
);

export default router;
