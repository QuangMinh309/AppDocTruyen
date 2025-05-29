import { sequelize } from '../../models/index.js'
import bcrypt from 'bcrypt'
import ApiError from '../../utils/api_error.util.js'
import {
  generateAccessToken,
  generateRefreshToken,
} from '../../utils/user.util.js'

const User = sequelize.models.User
const Role = sequelize.models.Role

const loginUser = async (email, password) => {
  try {
    const user = await User.findOne({
      where: { mail: email },
      include: [
        { model: Role, as: 'role', attributes: ['roleId', 'roleName'] },
      ],
    })

    if (!user) {
      throw new ApiError('Email hoặc mật khẩu không chính xác', 401)
    }

    if (user.status === 'locked') {
      throw new ApiError('Tài khoản của bạn đã bị khóa', 403)
    }

    const isPasswordValid = await bcrypt.compare(password, user.password)
    if (!isPasswordValid) {
      throw new ApiError('Email hoặc mật khẩu không chính xác', 401)
    }

    const accessToken = generateAccessToken(user)
    const refreshToken = generateRefreshToken(user)

    const { password: _, ...userDataWithoutPassword } = user.toJSON()
    return {
      user: userDataWithoutPassword,
      accessToken,
      refreshToken,
    }
  } catch (err) {
    if (err instanceof ApiError) throw err
    throw new ApiError('Lỗi khi đăng nhập', 500)
  }
}

export default loginUser
