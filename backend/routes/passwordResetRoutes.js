import express from "express";
import validate from "../middlewares/validate.js";
const router = express.Router();
import PasswordResetValidation from "../validators/passwordResetValidation.js";
import PasswordResetController from "../controllers/passwordResetController.js";

router.post('/', PasswordResetController.createPasswordReset);
router.post('/verify', PasswordResetController.verifyOTP);
router.get('/:OTP', PasswordResetController.getPasswordResetById);

export default router;