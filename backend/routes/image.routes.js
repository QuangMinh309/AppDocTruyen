import express from 'express';
import { uploadSingleImage } from '../middlewares/uploadImage.middleware.js';
import { getImageUrl, uploadImage, deleteImage } from '../controllers/image.controller.js';
import { uploadImageValidation } from '../validators/image.validation.js';
import { getImageUrlValidation } from '../validators/image.validation.js';

const router = express.Router();

// Route lấy URL ảnh từ Cloudinary
router.get('/:imageId', getImageUrlValidation, getImageUrl);

// Route upload ảnh lên Cloudinary
router.post(
  '/upload',
  uploadSingleImage('image'),
  uploadImageValidation,
  uploadImage
);

// Route xóa ảnh trên Cloudinary
router.delete('/:imageId', getImageUrlValidation, deleteImage);

// Export router
export default router;