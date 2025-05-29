import { sequelize } from '../../models/index.js'
import ApiError from '../../utils/api_error.util.js'
import { sendOTPEmail } from '../../utils/user.util.js'

const User = sequelize.models.User
const PasswordReset = sequelize.models.PasswordReset
const Parameter = sequelize.models.Parameter

const forgotPassword = async (email) => {
  try {
    const user = await User.findOne({ where: { mail: email } })
    if (!user) throw new ApiError('Email không tồn tại', 404)

    if (user.status === 'locked') {
      throw new ApiError('Tài khoản của bạn đã bị khóa', 403)
    }

    const parameters = await Parameter.findOne()
    if (!parameters) {
      throw new ApiError('Không tìm thấy tham số hệ thống', 500)
    }

    const otp = Math.floor(100000 + Math.random() * 900000) // 6-digit OTP
    const expiresAt = new Date(
      Date.now() + parameters.OTP_Valid_Period * 60 * 1000
    )

    await PasswordReset.create({
      OTP: otp,
      userId: user.userId,
      isUsed: false,
      createdAt: new Date(),
      expiresAt,
    })

    await sendOTPEmail(email, otp)
    return { message: 'Mã OTP đã được gửi đến email của bạn' }
  } catch (err) {
    if (err instanceof ApiError) throw err
    throw new ApiError('Lỗi khi gửi OTP', 500)
  }
}

export default forgotPassword
