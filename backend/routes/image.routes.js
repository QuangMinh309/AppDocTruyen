import express from 'express'
import multer from 'multer'
import path from 'path'
import { fileURLToPath } from 'url'
import { getImageUrl, uploadImage } from '../controllers/image.controller.js'

// Lấy __dirname trong ESM
const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)

// Khai báo multer để upload file
const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    const uploadPath = path.join(__dirname, '../uploads/')
    cb(null, uploadPath)
  },
  filename: (req, file, cb) => {
    const uniqueName =
      file.fieldname + '-' + Date.now() + path.extname(file.originalname)
    cb(null, uniqueName)
  },
})

const upload = multer({ storage })

const router = express.Router()

// Route lấy ảnh từ Cloudinary
router.get('/:imageId', getImageUrl)

// Route upload ảnh lên Cloudinary
router.post('/upload', upload.single('image'), uploadImage)

// Export router
export default router // Đảm bảo là export default
