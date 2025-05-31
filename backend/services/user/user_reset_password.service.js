import { sequelize } from '../../models/index.js'
import bcrypt from 'bcrypt'
import ApiError from '../../utils/api_error.util.js'

const User = sequelize.models.User
const PasswordReset = sequelize.models.PasswordReset

const resetPassword = async (otp, newPassword, confirmPassword) => {
  try {
    if (newPassword !== confirmPassword) {
      throw new ApiError('Mật khẩu xác nhận không khớp', 400)
    }

    const passwordReset = await PasswordReset.findOne({
      where: { OTP: otp, isUsed: false },
      include: [{ model: User, as: 'user' }],
    })

    if (!passwordReset) {
      throw new ApiError('Mã OTP không hợp lệ hoặc đã được sử dụng', 400)
    }

    if (new Date() > passwordReset.expiresAt) {
      throw new ApiError('Mã OTP đã hết hạn', 400)
    }

    if (passwordReset.user.status === 'locked') {
      throw new ApiError('Tài khoản của bạn đã bị khóa', 403)
    }

    const hashedPassword = await bcrypt.hash(newPassword, 10)
    await passwordReset.user.update({ password: hashedPassword })
    await passwordReset.update({ isUsed: true })

    return { message: 'Đặt lại mật khẩu thành công' }
  } catch (err) {
    if (err instanceof ApiError) throw err
    throw new ApiError('Lỗi khi đặt lại mật khẩu', 500)
  }
}

export default resetPassword
