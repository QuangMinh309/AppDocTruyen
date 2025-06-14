import express from 'express'
import ChapterController from '../controllers/chapter.controller.js'
import { authenticate } from '../middlewares/auth.middleware.js'
import {
  isStoryAuthor,
  canAccessChapter,
} from '../middlewares/permission.middleware.js'
import {
  validateCreateChapter,
  validateUpdateChapter,
  validateChapterId,
  validatePurchaseChapter,
  validateStoryIdParam,
} from '../validators/chapter.validation.js'
import validate from '../middlewares/validate.middleware.js'

const router = express.Router()

router.use(authenticate)

// Tạo chương mới
router.post(
  '/story/:storyId',
  validate(validateCreateChapter, 'body'),
  validate(validateStoryIdParam, 'params'),
  isStoryAuthor,
  ChapterController.createChapter
)

// Cập nhật chương
router.put(
  '/story/:storyId/:chapterId',
  validate(validateUpdateChapter, 'body'),
  validate(validateChapterId, 'params'),
  isStoryAuthor,
  ChapterController.updateChapter
)

// Xóa chương
router.delete(
  '/:chapterId',
  validate(validateChapterId, 'params'),
  isStoryAuthor,
  ChapterController.deleteChapter
)

// Đọc chương
router.get(
  '/:chapterId/read',
  validate(validateChapterId, 'params'),
  canAccessChapter,
  ChapterController.readChapter
)

// Mua chương
router.post(
  '/story/:storyId/:chapterId/purchase',
  validate(validatePurchaseChapter),
  ChapterController.purchaseChapter
)

export default router
