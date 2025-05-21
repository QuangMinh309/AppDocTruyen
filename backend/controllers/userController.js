import UserService from "../services/UserService.js";

const UserController = {
  async register(req, res, next) {
    try {
      const { userName, dUserName, mail, password, DOB } = req.body;

      const result = await UserService.registerUser({
        userName,
        dUserName: dUserName || userName,
        mail,
        password,
        DOB,
        // roleId: 2,
      });

      res.status(201).json({
        success: true,
        message: "Đăng ký thành công",
        data: result,
      });
    } catch (error) {
      next(error);
    }
  },

  async login(req, res, next) {
    try {
      const { mail, password } = req.body;

      const result = await UserService.loginUser(mail, password);

      res.status(200).json({
        success: true,
        message: "Đăng nhập thành công",
        data: result,
      });
    } catch (error) {
      next(error);
    }
  },

  async getCurrentUser(req, res, next) {
    try {
      const userId = req.user.userId;
      const user = await UserService.getUserById(userId);

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
      const user = await UserService.getUserById(parseInt(userId));

      res.status(200).json({
        success: true,
        data: user,
      });
    } catch (error) {
      next(error);
    }
  },

  async getAllUsers(req, res, next) {
    try {
      const offset = parseInt(req.query.offset) || 0;
      const limit = 15;

      const result = await UserService.getAllUsers(offset, limit);

      res.status(200).json({
        success: true,
        data: result,
      });
    } catch (error) {
      next(error);
    }
  },

  async updateUser(req, res, next) {
    try {
      const { userId } = req.params;
      const updatedData = req.body;

      delete updatedData.roleId;

      const user = await UserService.updateUser(parseInt(userId), updatedData);

      res.status(200).json({
        success: true,
        message: "Cập nhật người dùng thành công",
        data: user,
      });
    } catch (error) {
      next(error);
    }
  },

  async deleteUser(req, res, next) {
    try {
      const { userId } = req.params;

      const result = await UserService.deleteUser(parseInt(userId));

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

      const result = await UserService.changePassword(
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

  async togglePremium(req, res, next) {
    try {
      const { userId } = req.params;

      const result = await UserService.togglePremium(parseInt(userId));

      res.status(200).json({
        success: true,
        message: `Trạng thái premium đã ${result.isPremium ? "bật" : "tắt"}`,
        data: result,
      });
    } catch (error) {
      next(error);
    }
  },

  async refreshToken(req, res, next) {
    try {
      const userId = req.user.userId;

      const result = await UserService.refreshToken(userId);

      res.status(200).json({
        success: true,
        message: "Làm mới token thành công",
        data: result,
      });
    } catch (error) {
      next(error);
    }
  },
};

export default UserController;
