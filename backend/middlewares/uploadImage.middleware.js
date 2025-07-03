import multer from 'multer';
import path from 'path';

const storage = multer.memoryStorage(); // Lưu file vào bộ nhớ tạm
const limits = { fileSize: 5 * 1024 * 1024 }; // Giới hạn 5MB

const fileFilter = (req, file, cb) => {
  // console.log('File received:', file.originalname, file.mimetype);
  const filetypes = /jpeg|jpg|png|gif/;
  const extname = filetypes.test(path.extname(file.originalname).toLowerCase());
  const mimetype =
    filetypes.test(file.mimetype) || file.mimetype.startsWith('image/'); // Kiểm tra mimetype là ảnh

  if (extname && mimetype) {
    return cb(null, true);
  }
  cb(
    new Error(
      'Chỉ chấp nhận file ảnh (jpeg, jpg, png, gif) với định dạng hợp lệ!'
    )
  );
};

const upload = multer({ storage, limits, fileFilter });

// Upload ảnh đơn (single file)
export const uploadSingleImage =
  (fieldName = 'coverImgId') =>
  (req, res, next) => {
    // console.log('uploadSingleImage middleware called for field:', fieldName);
    upload.single(fieldName)(req, res, (err) => {
      if (err) {
        console.error('Multer error:', err);
        if (err instanceof multer.MulterError) {
          return res
            .status(400)
            .json({ message: 'Lỗi upload file: ' + err.message });
        }
        return res.status(400).json({ message: err.message });
      }
      // console.log('req.file:', req.file);
      // console.log('req.body:', req.body);
      next();
    });
  };

// Upload nhiều ảnh
export const uploadMultipleImages =
  (fieldName = 'images', maxCount = 5) =>
  (req, res, next) => {
    // console.log('uploadMultipleImages middleware called for field:', fieldName);
    upload.array(fieldName, maxCount)(req, res, (err) => {
      if (err) {
        console.error('Multer error:', err);
        if (err instanceof multer.MulterError) {
          return res
            .status(400)
            .json({ message: 'Lỗi upload file: ' + err.message });
        }
        return res.status(400).json({ message: err.message });
      }
      // console.log('req.files:', req.files);
      // console.log('req.body:', req.body);
      next();
    });
  };

// Upload ảnh avatar và background
export const uploadUserImages = (req, res, next) => {
  // console.log('uploadUserImages middleware called');
  upload.fields([
    { name: 'avatarId', maxCount: 1 },
    { name: 'backgroundId', maxCount: 1 },
  ])(req, res, (err) => {
    if (err) {
      console.error('Multer error:', err);
      return res
        .status(400)
        .json({ message: 'Lỗi upload file: ' + err.message });
    }
    // console.log('req.files:', req.files);
    // console.log('req.body:', req.body);
    next();
  });
};

export default { uploadSingleImage, uploadMultipleImages, uploadUserImages };
