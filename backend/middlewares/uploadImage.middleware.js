import multer from 'multer';
import path from 'path';

const upload = multer({
    storage: multer.memoryStorage(),
    limits: { fileSize: 5 * 1024 * 1024 }, // Giới hạn 5MB
    fileFilter: (req, file, cb) => {
        console.log('File received:', file.originalname, file.mimetype);
        const filetypes = /jpeg|jpg|png|gif/;
        const extname = filetypes.test(path.extname(file.originalname).toLowerCase());
        const mimetype = filetypes.test(file.mimetype);
        if (extname && mimetype) {
            return cb(null, true);
        }
        cb(new Error('Chỉ chấp nhận file ảnh (jpeg, jpg, png, gif)!'));
    },
});

const uploadHandler = (req, res, next) => {
    console.log('uploadHandler middleware called');
    upload.single('image')(req, res, (err) => {
        if (err) {
            console.error('Multer error:', err);
            if (err instanceof multer.MulterError) {
                return res.status(400).json({ message: 'Lỗi upload file: ' + err.message });
            }
            return res.status(400).json({ message: err.message });
        }
        console.log('req.file:', req.file);
        console.log('req.body:', req.body);
        console.log('Calling next()');
        next();
    });
};

export default uploadHandler;