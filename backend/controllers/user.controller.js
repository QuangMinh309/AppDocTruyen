import registerUser from '../services/user/user_register.service.js';
import loginUser from '../services/user/user_login.service.js';
import updateUser from '../services/user/user_update.service.js';
import getUserById from '../services/user/user_get_id.service.js';
import getCurrentUser from '../services/user/user_get_current.service.js';
import deleteUser from '../services/user/user_delete.service.js';
import changePassword from '../services/user/user_change_password.service.js';
import followUser from '../services/user/user_follow.service.js';
import unfollowUser from '../services/user/user_unfollow.service.js';
import purchasePremium from '../services/user/user_purchase_premium.service.js';
import refreshTokenUser from '../services/user/user_refresh_token.service.js';
import reportUser from '../services/user/user_report.service.js';
import checkFollowStatus from '../services/user/user_check_follow.service.js';
import PurchaseChapterService from '../services/user/user_purchase_chapter.service.js';
import WalletManagementService from '../services/user/user_wallet_management.service.js';
import CommentService from '../services/comment.service.js';

const UserController = {
  async register(req, res, next) {
    try {
      const { userName, mail, password, confirmPassword, DOB } = req.body;

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
      const data = req.body;

      const avatarFile = req.files?.avatarId?.[0] || null;
      const backgroundFile = req.files?.backgroundId?.[0] || null;

      // Nếu không phải admin, không được cập nhật roleId
      if (req.user.role.roleName !== 'admin') {
        delete data.roleId;
      }

      const user = await updateUser(
        parseInt(userId),
        data,
        avatarFile,
        backgroundFile
      );

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

  async followUser(req, res, next) {
    try {
      const  followedId  = req.body.id;
      const followerId = req.user.userId;

      const result = await followUser(followerId, parseInt(followedId));

      res.status(200).json({
        status: 200,
        message: result.message,
      });
    } catch (error) {
      next(error);
    }
  },

  async unlikeComment(req, res, next) {
    try {
      const commentId = req.body.id;
      const userId = req.user.userId;

      const result = await CommentService.unlikeComment(parseInt(commentId),userId);

      res.status(200).json({
        status: 200,
        message: result.message,
      });
    } catch (error) {
      next(error);
    }
  },

  async likeComment(req, res, next) {
    try {
       const commentId = req.body.id;
      const userId = req.user.userId;
      console.log('likeComment', commentId, userId)

      const result = await CommentService.likeComment(parseInt(commentId),userId);

      res.status(200).json({
        status: 200,
        message: result.message,
      });
    } catch (error) {
      next(error);
    }
  },

  async unfollowUser(req, res, next) {
    try {
      const  followedId  = req.body.id;
      const followerId = req.user.userId;

      const result = await unfollowUser(followerId, parseInt(followedId));

      res.status(200).json({
        status: 200,
        message: result.message,
      });
    } catch (error) {
      next(error);
    }
  },

  async getFollowStatus(req, res, next) {
    try {
      const { followedId } = req.params;
      const followerId = req.user.userId;

      const result = await checkFollowStatus(followerId, Number(followedId));

      return res.status(200).json(result);
    } catch (error) {
      next(error);
    }
  },

  async PurchaseChapter(req, res, next) {
    try {
      const userId = req.user.userId;
      const chapterId = req.params.chapterId

      const result = await PurchaseChapterService.purchaseChapter(userId, chapterId);

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

  async walletChange(req, res, next) {
    try {
      const userId = req.user.userId;
      const data = req.body
      console.log(data)
      const result = await WalletManagementService.walletChange(userId, data);

      res.status(200).json({
        success: true,
        message: result.message,
      });
    } catch (error) {
      next(error);
    }
  },


  async reportUser(req, res, next) {
    try {
      const { reportedUserId, reason } = req.body;
      const reporterId = req.user.userId;

      if (!reportedUserId || !reason) {
        throw new ApiError('Vui lòng điền đầy đủ thông tin', 400);
      }

      const result = await reportUser(reason, reporterId, reportedUserId);

      return res.status(201).json({
        success: true,
        message: result.message,
        data: {
          reportedUserId: result.reportedUserId,
          reason: result.reason,
        },
      });
    } catch (error) {
      next(error);
    }
  },
};

export default UserController;
