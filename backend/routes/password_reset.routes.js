import express from 'express'
import validate from '../middlewares/validate.middleware.js'
const router = express.Router()
import PasswordResetValidation from '../validators/password_reset.validation.js'
import PasswordResetController from '../controllers/password_reset.controller.js'

router.post('/', PasswordResetController.createPasswordReset)
router.post('/verify', PasswordResetController.verifyOTP)
router.get('/:OTP', PasswordResetController.getPasswordResetById)

export default router
