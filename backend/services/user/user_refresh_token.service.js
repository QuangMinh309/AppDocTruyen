import { sequelize } from '../../models/index.js';
import jwt from 'jsonwebtoken';
import ApiError from '../../utils/api_error.util.js';
import { generateAccessToken, validateUser } from '../../utils/user.util.js';

const User = sequelize.models.User;
const Role = sequelize.models.Role;

const refreshTokenUser = async (refreshToken) => {
  try {
    const decoded = jwt.verify(refreshToken, process.env.JWT_REFRESH_SECRET);
    const user = await User.findByPk(decoded.userId, {
      include: [
        { model: Role, as: 'role', attributes: ['roleId', 'roleName'] },
      ],
    });

    validateUser(user.userId);

    const accessToken = generateAccessToken(user);
    return { accessToken };
  } catch (err) {
    if (err instanceof jwt.JsonWebTokenError) {
      // console.log(err);
      throw new ApiError('Refresh token không hợp lệ', 401);
    }
    if (err instanceof jwt.TokenExpiredError) {
      throw new ApiError('Refresh token đã hết hạn', 401);
    }
    throw new ApiError('Lỗi khi làm mới token', 500);
  }
};

export default refreshTokenUser;
