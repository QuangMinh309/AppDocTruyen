import PasswordResetService from '../services/password_reset.service.js'
import passwordResetSchema from '../validators/password_reset.validation.js'

const PasswordResetController = {
  async createPasswordReset(req, res, next) {
    try {
      const passwordReset = await PasswordResetService.createPasswordReset(
        req.body
      )
      res.status(201).json(passwordReset)
    } catch (error) {
      next(error)
    }
  },

  async verifyOTP(req, res, next) {
    try {
      const { OTP, userId } = req.body
      const result = await PasswordResetService.verifyOTP(OTP, userId)
      res.status(200).json(result)
    } catch (error) {
      next(error)
    }
  },

  async getPasswordResetById(req, res, next) {
    try {
      const passwordReset = await PasswordResetService.getPasswordResetById(
        req.params.OTP
      )
      res.status(200).json(passwordReset)
    } catch (error) {
      next(error)
    }
  },
}

export default PasswordResetController
