import express from 'express'
import UserController from '../controllers/user.controller.js'
import validate from '../middlewares/validate.middleware.js'
import validators from '../validators/user.validation.js'
import {
  authenticate,
  authenticateRefreshToken,
  isResourceOwner,
} from '../middlewares/auth.middleware.js'
import { validatePurchaseChapter } from '../validators/chapter.validation.js';
import { uploadUserImages } from '../middlewares/uploadImage.middleware.js'

const router = express.Router()

// Tuyến đường không cần xác thực
router.post('/register', validate(validators.register), UserController.register)

router.post('/login', validate(validators.login), UserController.login)

router.post(
  '/refresh-token',
  validate(validators.refreshToken),
  authenticateRefreshToken,
  UserController.refreshTokenUser
)

// Tuyến đường yêu cầu xác thực (cho user thường và admin)
router.use(authenticate)

router.get('/me', UserController.getCurrentUser)

router.get(
  '/:userId',
  validate(validators.userId, 'params'),
  UserController.getUserById
)

router.put(
  '/:userId',
  uploadUserImages,
  validate(validators.userId, 'params'),
  validate(validators.updateUser),
  isResourceOwner,
  UserController.updateUser
)

router.delete(
  '/:userId',
  validate(validators.userId, 'params'),
  isResourceOwner,
  UserController.deleteUser
)

router.post(
  '/change-password/:userId',
  validate(validators.userId, 'params'),
  validate(validators.changePassword),
  isResourceOwner,
  UserController.changePassword
)

router.post(
  '/follow',
  validate(validators.followUser),
  UserController.followUser
)

router.post(
  '/unfollow',
  validate(validators.followUser),
  UserController.unfollowUser
)


router.post(
  '/like-comment',
  validate(validators.comment),
  UserController.likeComment
)

router.post(
  '/unlike-comment',
  validate(validators.comment),
  UserController.unlikeComment
)

router.get('/follow/status/:followedId', UserController.getFollowStatus)

router.post('/purchase-premium', UserController.purchasePremium)

router.post('/purchase-chapter/:chapterId', validate(validatePurchaseChapter, "params"), UserController.PurchaseChapter)

router.post('/wallet', validate(validators.walletChange), UserController.walletChange)

router.post('/report', UserController.reportUser)

export default router
