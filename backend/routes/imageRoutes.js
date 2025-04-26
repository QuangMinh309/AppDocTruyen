import express from 'express';
import multer from 'multer';
import path from 'path';
import { fileURLToPath } from 'url';
import { getImageUrl, uploadImage } from '../controllers/imageController.js';

// Lấy __dirname trong ESM
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Cấu hình multer để lưu tạm thời file upload
const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, path.join(__dirname, '../uploads/')); // Đảm bảo đường dẫn đúng
  },
  filename: (req, file, cb) => {
    cb(null, file.fieldname + '-' + Date.now() + path.extname(file.originalname));
  },
});

const upload = multer({ storage });

const router = express.Router();

// Route lấy ảnh từ Cloudinary
router.get('/:imageId', getImageUrl);

// Route upload ảnh lên Cloudinary
router.post('/upload', upload.single('image'),uploadImage);

// Export router
export default router;  // Đảm bảo là export default
