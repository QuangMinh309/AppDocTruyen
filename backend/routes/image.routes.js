import express from 'express'
import multer from 'multer'
import path from 'path'
import uploadHandler from '../middlewares/uploadImage.middleware.js';
import validate from '../middlewares/validate.middleware.js'
import { fileURLToPath } from 'url'
import { getImageUrl, uploadImage, deleteImage } from '../controllers/image.controller.js'
import { uploadImageValidation } from '../validators/image.validation.js'
import { getImageUrlValidation } from '../validators/image.validation.js'

const router = express.Router()

// Route lấy ảnh từ Cloudinary
router.get('/:imageId', getImageUrlValidation, getImageUrl)

// Route upload ảnh lên Cloudinary
router.post(
  '/upload',
  uploadHandler,
  uploadImageValidation,
  uploadImage
);

// Route xóa ảnh trên Cloudinary
router.delete('/:imageId', getImageUrlValidation, deleteImage)

// Export router
export default router // Đảm bảo là export default
