import jwt from 'jsonwebtoken'
import { sequelize } from '../models/index.js'
import ApiError from '../utils/api_error.util.js'

const User = sequelize.models.User
const Role = sequelize.models.Role
const Parameter = sequelize.models.Parameter

// Xác thực access token
export const authenticate = async (req, res, next) => {
  try {
    const authHeader = req.headers.authorization

    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return next(new ApiError('Không có token xác thực', 401))
    }

    const token = authHeader.split(' ')[1]
    const decoded = jwt.verify(token, process.env.JWT_SECRET)

    const user = await User.findByPk(decoded.userId, {
      attributes: { exclude: ['password'] },
      include: [
        { model: Role, as: 'role', attributes: ['roleId', 'roleName'] },
      ],
    })

    if (!user) {
      return next(new ApiError('Người dùng không tồn tại', 404))
    }

    if (user.status === 'locked') {
      return next(new ApiError('Tài khoản của bạn đã bị khóa', 403))
    }

    // Kiểm tra thời hạn premium nếu người dùng là premium
    if (user.isPremium && user.premiumExpiresAt) {
      const now = new Date()
      if (now > new Date(user.premiumExpiresAt)) {
        await user.update({ isPremium: false, premiumExpiresAt: null })
      }
    }

    req.user = user
    next()
  } catch (error) {
    if (error instanceof jwt.JsonWebTokenError) {
      return next(new ApiError('Token không hợp lệ', 401))
    }
    if (error instanceof jwt.TokenExpiredError) {
      return next(new ApiError('Token đã hết hạn', 401))
    }
    console.error('Lỗi xác thực:', error)
    return next(new ApiError(`Lỗi xác thực: ${error.message}`, 500))
  }
}

// Xác thực refresh token
export const authenticateRefreshToken = async (req, res, next) => {
  try {
    const { refreshToken } = req.body

    if (!refreshToken) {
      return next(new ApiError('Không có refresh token', 401))
    }

    const decoded = jwt.verify(refreshToken, process.env.JWT_REFRESH_SECRET)

    const user = await User.findByPk(decoded.userId, {
      attributes: { exclude: ['password'] },
      include: [
        { model: Role, as: 'role', attributes: ['roleId', 'roleName'] },
      ],
    })

    if (!user) {
      return next(new ApiError('Người dùng không tồn tại', 404))
    }

    if (user.status === 'locked') {
      return next(new ApiError('Tài khoản của bạn đã bị khóa', 403))
    }

    req.user = user
    next()
  } catch (error) {
    if (error instanceof jwt.JsonWebTokenError) {
      return next(new ApiError('Refresh token không hợp lệ', 401))
    }
    if (error instanceof jwt.TokenExpiredError) {
      return next(new ApiError('Refresh token đã hết hạn', 401))
    }
    console.error('Lỗi xác thực refresh token:', error)
    return next(
      new ApiError(`Lỗi xác thực refresh token: ${error.message}`, 500)
    )
  }
}

// Kiểm tra vai trò
export const authorizeRoles = (...roles) => {
  return (req, res, next) => {
    if (!req.user || !req.user.role) {
      return next(new ApiError('Không có quyền truy cập', 403))
    }

    if (!roles.includes(req.user.role.roleName)) {
      return next(
        new ApiError('Bạn không có quyền thực hiện hành động này', 403)
      )
    }

    next()
  }
}

// Kiểm tra quyền sở hữu tài nguyên
export const isResourceOwner = (req, res, next) => {
  const userId = parseInt(req.params.userId, 10)

  if (isNaN(userId)) {
    return next(new ApiError('ID người dùng không hợp lệ', 400))
  }

  if (req.user.userId !== userId && req.user.role.roleName !== 'admin') {
    return next(new ApiError('Bạn không có quyền truy cập tài nguyên này', 403))
  }

  next()
}

export const isPremiumUser = async (req, res, next) => {
  if (!req.user.isPremium) {
    return next(new ApiError('Yêu cầu tài khoản premium để truy cập', 403))
  }

  next()
}
