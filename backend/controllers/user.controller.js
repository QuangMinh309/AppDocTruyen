import registerUser from '../services/user/user_register.service.js';
import loginUser from '../services/user/user_login.service.js';
import updateUser from '../services/user/user_update.service.js';
import getUserById from '../services/user/user_get_id.service.js';
import getCurrentUser from '../services/user/user_get_current.service.js';
import deleteUser from '../services/user/user_delete.service.js';
import forgotPassword from '../services/user/user_forgot_password.service.js';
import resetPassword from '../services/user/user_reset_password.service.js';
import changePassword from '../services/user/user_change_password.service.js';
import followUser from '../services/user/user_follow.service.js';
import unfollowUser from '../services/user/user_unfollow.service.js';
import purchasePremium from '../services/user/user_purchase_premium.service.js';
import AdminService from '../services/admin/admin.service.js';
import refreshTokenUser from '../services/user/user_refresh_token.service.js';

const UserController = {
  async register(req, res, next) {
    try {
      const { password, confirmPassword } = req.body;

      // Kiểm tra xác nhận mật khẩu
      if (password !== confirmPassword) {
        throw new ApiError('Mật khẩu xác nhận không khớp', 400);
      }

      await registerUser({
        userName,
        mail,
        password,
        DOB,
      });

      res.status(201).json({
        success: true,
        message: 'Đăng ký thành công',
      });
    } catch (error) {
      next(error);
    }
  },

  async login(req, res, next) {
    try {
      const { mail, password } = req.body;

      const result = await loginUser(mail, password);

      res.status(200).json({
        success: true,
        message: 'Đăng nhập thành công',
        data: result,
      });
    } catch (error) {
      next(error);
    }
  },

  async refreshTokenUser(req, res, next) {
    try {
      const { refreshToken } = req.body;

      const result = await refreshTokenUser(refreshToken);

      res.status(200).json({
        success: true,
        message: 'Làm mới token thành công',
        data: result,
      });
    } catch (error) {
      next(error);
    }
  },

  async getCurrentUser(req, res, next) {
    try {
      const userId = req.user.userId;
      const user = await getCurrentUser(userId);

      res.status(200).json({
        success: true,
        data: user,
      });
    } catch (error) {
      next(error);
    }
  },

  async getUserById(req, res, next) {
    try {
      const { userId } = req.params;
      const user = await getUserById(parseInt(userId));

      res.status(200).json({
        success: true,
        data: user,
      });
    } catch (error) {
      next(error);
    }
  },

  async updateUser(req, res, next) {
    try {
      const { userId } = req.params;
      const updatedData = req.body;

      // Nếu không phải admin, không được cập nhật roleId
      if (req.user.role.roleName !== 'admin') {
        delete updatedData.roleId;
      }

      const user = await updateUser(parseInt(userId), updatedData);

      res.status(200).json({
        success: true,
        message: 'Cập nhật người dùng thành công',
        data: user,
      });
    } catch (error) {
      next(error);
    }
  },

  async deleteUser(req, res, next) {
    try {
      const { userId } = req.params;

      const result = await deleteUser(parseInt(userId));

      res.status(200).json({
        success: true,
        message: result.message,
      });
    } catch (error) {
      next(error);
    }
  },

  async changePassword(req, res, next) {
    try {
      const { userId } = req.params;
      const { currentPassword, newPassword } = req.body;

      const result = await changePassword(
        parseInt(userId),
        currentPassword,
        newPassword
      );

      res.status(200).json({
        success: true,
        message: result.message,
      });
    } catch (error) {
      next(error);
    }
  },

  async forgotPassword(req, res, next) {
    try {
      const { mail } = req.body;

      const result = await forgotPassword(mail);

      res.status(200).json({
        success: true,
        message: result.message,
      });
    } catch (error) {
      next(error);
    }
  },

  async resetPassword(req, res, next) {
    try {
      const { otp, newPassword, confirmPassword } = req.body;

      const result = await resetPassword(otp, newPassword, confirmPassword);

      res.status(200).json({
        success: true,
        message: result.message,
      });
    } catch (error) {
      next(error);
    }
  },

  async followUser(req, res, next) {
    try {
      const { followedId } = req.body;
      const followerId = req.user.userId;

      const result = await followUser(followerId, parseInt(followedId));

      res.status(200).json({
        success: true,
        message: result.message,
      });
    } catch (error) {
      next(error);
    }
  },

  async unfollowUser(req, res, next) {
    try {
      const { followedId } = req.body;
      const followerId = req.user.userId;

      const result = await unfollowUser(followerId, parseInt(followedId));

      res.status(200).json({
        success: true,
        message: result.message,
      });
    } catch (error) {
      next(error);
    }
  },

  async purchasePremium(req, res, next) {
    try {
      const userId = req.user.userId;

      const result = await purchasePremium(userId);

      res.status(200).json({
        success: true,
        message: result.message,
      });
    } catch (error) {
      next(error);
    }
  },

  async getAllUsers(req, res, next) {
    try {
      const offset = parseInt(req.query.offset) || 0;
      const limit = 15;

      const result = await AdminService.getAllUsers(offset, limit);

      res.status(200).json({
        success: true,
        data: result,
      });
    } catch (error) {
      next(error);
    }
  },

  async lockUser(req, res, next) {
    try {
      const { userId } = req.params;

      const result = await AdminService.lockUser(parseInt(userId));

      res.status(200).json({
        success: true,
        message: result.message,
      });
    } catch (error) {
      next(error);
    }
  },

  async unlockUser(req, res, next) {
    try {
      const { userId } = req.params;

      const result = await AdminService.unlockUser(parseInt(userId));

      res.status(200).json({
        success: true,
        message: result.message,
      });
    } catch (error) {
      next(error);
    }
  },
};

export default UserController;
