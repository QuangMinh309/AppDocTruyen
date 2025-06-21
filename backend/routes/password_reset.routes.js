import express from 'express';
import validate from '../middlewares/validate.middleware.js';
import PasswordResetValidation from '../validators/password_reset.validation.js';
import PasswordResetController from '../controllers/password_reset.controller.js';

const router = express.Router();

// Route tạo mã reset mật khẩu
router.post('/create', validate(PasswordResetValidation.create), PasswordResetController.createPasswordReset);

// Route gửi OTP
router.post('/send-otp', validate(PasswordResetValidation.sendOTP), PasswordResetController.sendOTP);

// Route xác minh OTP
router.post('/verify-otp', validate(PasswordResetValidation.verify), PasswordResetController.verifyOTP);

// Route lấy thông tin mã OTP theo ID
router.get('/:OTP', validate(PasswordResetValidation.idSchema, 'params'), PasswordResetController.getPasswordResetById);

// Route đặt lại mật khẩu
router.post('/reset', validate(PasswordResetValidation.reset), PasswordResetController.resetPassword);

export default router;