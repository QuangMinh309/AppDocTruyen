import express from 'express'
import UserController from '../controllers/user.controller.js'
import validate from '../middlewares/validate.middleware.js'
import validators from '../validators/user.validation.js'
import {
  authenticate,
  authenticateRefreshToken,
  authorizeRoles,
  isResourceOwner,
} from '../middlewares/auth.middleware.js'

const router = express.Router()

// Tuyến đường không cần xác thực
router.post('/register', validate(validators.register), UserController.register)

router.post('/login', validate(validators.login), UserController.login)

router.post(
  '/forgot-password',
  validate(validators.forgotPassword),
  UserController.forgotPassword
)

router.post(
  '/reset-password',
  validate(validators.resetPassword),
  UserController.resetPassword
)

router.post(
  '/refresh-token',
  validate(validators.refreshToken),
  authenticateRefreshToken,
  UserController.refreshToken
)

// Tuyến đường yêu cầu xác thực (cho user thường và admin)
router.get('/me', authenticate, UserController.getCurrentUser)

router.get(
  '/:userId',
  validate(validators.userId, 'params'),
  authenticate,
  UserController.getUserById
)

router.put(
  '/:userId',
  validate(validators.userId, 'params'),
  validate(validators.updateUser),
  authenticate,
  isResourceOwner,
  UserController.updateUser
)

router.delete(
  '/:userId',
  validate(validators.userId, 'params'),
  authenticate,
  isResourceOwner,
  UserController.deleteUser
)

router.post(
  '/change-password/:userId',
  validate(validators.userId, 'params'),
  validate(validators.changePassword),
  authenticate,
  isResourceOwner,
  UserController.changePassword
)

router.post(
  '/follow',
  validate(validators.followUser),
  authenticate,
  UserController.followUser
)

router.post(
  '/unfollow',
  validate(validators.followUser),
  authenticate,
  UserController.unfollowUser
)

router.post('/purchase-premium', authenticate, UserController.purchasePremium)

// Tuyến đường chỉ dành cho admin
router.get(
  '/',
  authenticate,
  authorizeRoles('admin'),
  UserController.getAllUsers
)

router.post(
  '/lock/:userId',
  validate(validators.userId, 'params'),
  authenticate,
  authorizeRoles('admin'),
  UserController.lockUser
)

router.post(
  '/unlock/:userId',
  validate(validators.userId, 'params'),
  authenticate,
  authorizeRoles('admin'),
  UserController.unlockUser
)

export default router
