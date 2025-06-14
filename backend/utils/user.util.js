import { sequelize } from '../models/index.js'
import jwt from 'jsonwebtoken'
import nodemailer from 'nodemailer'
import ApiError from './api_error.util.js'

const User = sequelize.models.User

const generateAccessToken = (user) => {
  const payload = {
    userId: user.userId,
    roleId: user.roleId,
  }
  return jwt.sign(payload, process.env.JWT_SECRET, { expiresIn: '24h' })
}

const generateRefreshToken = (user) => {
  const payload = {
    userId: user.userId,
    roleId: user.roleId,
  }
  return jwt.sign(payload, process.env.JWT_REFRESH_SECRET, {
    expiresIn: '7d',
  })
}

const validateFollowOperation = async (followerId, followedId) => {
  const follower = await User.findByPk(followerId)
  const followed = await User.findByPk(followedId)

  if (!follower || !followed) {
    throw new ApiError('Người dùng không tồn tại', 404)
  }

  if (follower.status === 'locked' || followed.status === 'locked') {
    throw new ApiError('Tài khoản đã bị khóa', 403)
  }

  return { follower, followed }
}

// Kiểm tra người dùng tồn tại
const validateUser = async (userId) => {
  const user = await User.findByPk(userId)
  if (!user) throw new ApiError('Người dùng không tồn tại', 404)
  if (user.status === 'locked') {
    throw new ApiError(
      'Tài khoản của bạn đã bị khóa, không thể thực hiện thao tác',
      403
    )
  }
  return user
}

export {
  generateAccessToken,
  generateRefreshToken,
  validateUser,
  validateFollowOperation,
}
