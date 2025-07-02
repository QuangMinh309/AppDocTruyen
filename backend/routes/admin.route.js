import express from 'express';
import AdminController from '../controllers/admin.controller.js';
import validate from '../middlewares/validate.middleware.js';
import validators from '../validators/user.validation.js';
import {
  authenticate,
  authorizeRoles,
} from '../middlewares/auth.middleware.js';
import { getDailyRevenueQuery } from '../validators/report.validation.js';

const router = express.Router();

router.use(authenticate);

// Route quản lý người dùng
router.get('/', authorizeRoles('admin'), AdminController.getAllUsers);

router.post(
  '/lock/:userId',
  validate(validators.userId, 'params'),
  authorizeRoles('admin'),
  AdminController.lockUser
);

router.post(
  '/unlock/:userId',
  validate(validators.userId, 'params'),
  authorizeRoles('admin'),
  AdminController.unlockUser
);

// Route quản lý truyện
router.put(
  '/:storyId/approve',
  authorizeRoles('admin'),
  AdminController.approveStory
);

// Route quản lý giao dịch
router.put(
  '/:transactionId/approve-trans',
  authorizeRoles('admin'),
  AdminController.approveTransaction
);

router.get(
  '/revenue/daily',
  validate(getDailyRevenueQuery, 'query'),
  authorizeRoles('admin'),
  AdminController.getDailyRevenueByMonth
);

export default router;
