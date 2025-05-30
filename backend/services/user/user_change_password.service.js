import bcrypt from 'bcrypt'
import ApiError from '../../utils/api_error.util.js'
import { validateUser } from '../../utils/user.util.js'

const changePassword = async (userId, currentPassword, newPassword) => {
  try {
    const user = await validateUser(userId)

    const isPasswordValid = await bcrypt.compare(currentPassword, user.password)
    if (!isPasswordValid) {
      throw new ApiError('Mật khẩu hiện tại không chính xác', 400)
    }

    const hashedPassword = await bcrypt.hash(newPassword, 10)
    await user.update({ password: hashedPassword })

    return { message: 'Đổi mật khẩu thành công' }
  } catch (err) {
    if (err instanceof ApiError) throw err
    throw new ApiError('Lỗi khi đổi mật khẩu', 500)
  }
}

export default changePassword
