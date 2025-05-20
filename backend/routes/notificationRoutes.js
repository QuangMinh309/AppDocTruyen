import express from "express";
import validate from "../middlewares/validate.js";
const router = express.Router();
import NotificationValidation from "../validators/notificationValidation.js";
import { NotificationController } from "../controllers/notificationController.js";

router.post('/', validate(NotificationValidation.create), NotificationController.createNotification);
router.get('/:notificationId', validate(NotificationValidation.idSchema), NotificationController.getNotificationById);
router.get('/', validate(NotificationValidation.idSchema), NotificationController.getAllNotifications);
router.put('/:notificationId', validate(NotificationValidation.update), NotificationController.updateNotification);
router.delete('/:notificationId', validate(NotificationValidation.idSchema), NotificationController.deleteNotification);

export default router;