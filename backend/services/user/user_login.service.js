import { sequelize } from '../../models/index.js'
import bcrypt from 'bcrypt'
import ApiError from '../../utils/api_error.util.js'
import {
  generateAccessToken,
  generateRefreshToken,
} from '../../utils/user.util.js'
import userPremiumService from './user_premium_update.service.js'

const User = sequelize.models.User
const Role = sequelize.models.Role
const Story = sequelize.models.Story
const NameList = sequelize.models.NameList

const loginUser = async (email, password) => {
  try {
    const normalizedEmail = email.toLowerCase()

    let user = await User.findOne({
      where: { mail: normalizedEmail },
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


    if (user.isPremium) {
      // console.log(`user premium ${user.isPremium}`)
      await userPremiumService.UpdateUserPremiumInfo(user.userId)
      user = await User.findByPk(user.userId)
    }


    const storyCount = await Story.count({ where: { userId: user.userId } })
    const nameListCount = await NameList.count({ where: { userId: user.userId } })

    const accessToken = generateAccessToken(user)
    const refreshToken = generateRefreshToken(user)

    const { password: _, DOB, ...rest } = user.toJSON()

    const formattedDob = DOB ? new Date(DOB).toISOString().split('T')[0] : null

    const userDataWithoutPassword = {
      ...rest,
      DOB: formattedDob,
      storyCount,
      nameListCount,
    }

    return {
      user: userDataWithoutPassword,
      accessToken,
      refreshToken,
    }
  } catch (err) {
    // console.log(err)
    if (err instanceof ApiError) throw err
    throw new ApiError('Lỗi khi đăng nhập', 500)
  }
}

export default loginUser
