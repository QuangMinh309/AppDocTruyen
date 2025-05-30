import { sequelize } from '../../models/index.js'
import bcrypt from 'bcrypt'
import ApiError from '../../utils/api_error.util.js'

const User = sequelize.models.User

const registerUser = async (data) => {
  try {
    const { password, userName, dUserName, ...userData } = data

    // Check if email already exists
    const existingUser = await User.findOne({
      where: { mail: userData.mail },
    })
    if (existingUser) {
      throw new ApiError('Email đã được sử dụng', 400)
    }

    const hashedPassword = await bcrypt.hash(password, 10)
    await User.create({
      ...userData,
      userName,
      password: hashedPassword,
      dUserName: dUserName || userName,
      followerNum: 0,
      wallet: 0,
      isPremium: false,
      status: 'active',
    })

    return true
  } catch (err) {
    if (err instanceof ApiError) throw err
    throw new ApiError('Lỗi khi đăng ký', 500)
  }
}

export default registerUser
