import { sequelize } from "../models/index.js";
const User = sequelize.models.User;
const Role = sequelize.models.Role;
const Story = sequelize.models.Story;
const Transaction = sequelize.models.Transaction;
import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";
import ApiError from "../utils/apiError.js";

const UserService = {
  generateToken(user) {
    const payload = {
      userId: user.userId,
      roleId: user.roleId,
    };

    // Tạo token với thời hạn 24h
    return jwt.sign(payload, process.env.JWT_SECRET, { expiresIn: "24h" });
  },

  async registerUser(data) {
    try {
      const { password, ...userData } = data;

      // Kiểm tra email
      const existingUser = await User.findOne({
        where: { mail: userData.mail },
      });
      if (existingUser) {
        throw new ApiError("Email đã được sử dụng", 400);
      }

      const hashedPassword = await bcrypt.hash(password, 10);
      const user = await User.create({
        ...userData,
        password: hashedPassword,
        followerNum: 0,
        wallet: 0,
        isPremium: false,
      });

      // Tạo token
      const token = this.generateToken(user);

      // Trả về người dùng, ẩn password
      const { password: _, ...userDataWithoutPassword } = user.toJSON();
      return {
        user: userDataWithoutPassword,
        token,
      };
    } catch (err) {
      if (err instanceof ApiError) throw err;
      throw new ApiError("Lỗi khi tạo người dùng", 500);
    }
  },

  async loginUser(email, password) {
    try {
      // Tìm người dùng bằng email
      const user = await User.findOne({
        where: { mail: email },
        include: [
          { model: Role, as: "role", attributes: ["roleId", "roleName"] },
        ],
      });

      if (!user) {
        throw new ApiError("Email hoặc mật khẩu không chính xác", 401);
      }

      // So sánh password
      const isPasswordValid = await bcrypt.compare(password, user.password);
      if (!isPasswordValid) {
        throw new ApiError("Email hoặc mật khẩu không chính xác", 401);
      }

      // Tạo token
      const token = this.generateToken(user);

      const { password: _, ...userDataWithoutPassword } = user.toJSON();
      return {
        user: userDataWithoutPassword,
        token,
      };
    } catch (err) {
      if (err instanceof ApiError) throw err;
      throw new ApiError("Lỗi khi đăng nhập", 500);
    }
  },

  async createUser(data) {
    try {
      const { password, ...userData } = data;

      // Kiểm tra email
      const existingUser = await User.findOne({
        where: { mail: userData.mail },
      });
      if (existingUser) {
        throw new ApiError("Email đã được sử dụng", 400);
      }

      const hashedPassword = await bcrypt.hash(password, 10);
      const user = await User.create({
        ...userData,
        password: hashedPassword,
        followerNum: 0,
      });

      const { password: _, ...userDataWithoutPassword } = user.toJSON();
      return userDataWithoutPassword;
    } catch (err) {
      if (err instanceof ApiError) throw err;
      throw new ApiError("Lỗi khi tạo người dùng", 500);
    }
  },

  async getUserById(userId) {
    try {
      const user = await User.findByPk(userId, {
        attributes: {
          exclude: ["password"],
        },
        include: [
          { model: Role, as: "role", attributes: ["roleId", "roleName"] },
          { model: Story, as: "stories", attributes: ["storyId", "storyName"] },
          // {
          //   model: Transaction,
          //   as: "transactions",
          //   attributes: ["transactionId", "money", "status", "createdAt"],
          // },
        ],
      });

      if (!user) throw new ApiError("Người dùng không tồn tại", 404);
      return user.toJSON();
    } catch (err) {
      if (err instanceof ApiError) throw err;
      console.error("Error in getUserById:", err);
      throw new ApiError("Lỗi khi lấy thông tin người dùng", 500);
    }
  },

  async getUserByEmail(mail) {
    try {
      const user = await User.findOne({
        where: { mail },
        attributes: {
          exclude: ["password"],
        },
        include: [
          { model: Role, as: "role", attributes: ["roleId", "roleName"] },
          { model: Story, as: "stories", attributes: ["storyId", "storyName"] },
          {
            model: Transaction,
            as: "transactions",
            attributes: ["transactionId", "money", "status", "createdAt"],
          },
        ],
      });

      if (!user) throw new ApiError("Người dùng không tồn tại", 404);
      return user.toJSON();
    } catch (err) {
      if (err instanceof ApiError) throw err;
      throw new ApiError("Lỗi khi lấy thông tin người dùng", 500);
    }
  },

  async getAllUsers(offset = 0, limit = 15) {
    try {
      const users = await User.findAll({
        offset,
        limit,
        order: [["createdAt", "DESC"]],
        attributes: { exclude: ["password"] },
        include: [
          {
            model: Role,
            as: "role",
            attributes: ["roleId", "roleName"],
          },
        ],
      });

      return users.map((user) => user.toJSON());
    } catch (err) {
      throw new ApiError("Lỗi khi lấy người dùng", 500);
    }
  },

  async updateUser(userId, data) {
    try {
      const user = await User.findByPk(userId);
      if (!user) throw new ApiError("Người dùng không tồn tại", 404);

      const { password, ...updateData } = data;

      // Kiểm tra email khi cập nhật
      if (updateData.mail && updateData.mail !== user.mail) {
        const existingUser = await User.findOne({
          where: { mail: updateData.mail },
        });
        if (existingUser) {
          throw new ApiError("Email đã được sử dụng bởi người dùng khác", 400);
        }
      }

      // Đổi password thì băm lại
      if (password) {
        updateData.password = await bcrypt.hash(password, 10);
      }

      await user.update(updateData);

      const updatedUser = await User.findByPk(userId, {
        attributes: { exclude: ["password"] },
        include: [
          { model: Role, as: "role", attributes: ["roleId", "roleName"] },
        ],
      });

      return updatedUser.toJSON();
    } catch (err) {
      if (err instanceof ApiError) throw err;
      throw new ApiError("Lỗi khi cập nhật người dùng", 500);
    }
  },

  async deleteUser(userId) {
    try {
      const user = await User.findByPk(userId);
      if (!user) throw new ApiError("Người dùng không tồn tại", 404);

      await user.destroy();
      return { message: "Xóa người dùng thành công" };
    } catch (err) {
      if (err instanceof ApiError) throw err;
      throw new ApiError("Lỗi khi xóa người dùng", 500);
    }
  },

  async changePassword(userId, currentPassword, newPassword) {
    try {
      const user = await User.findByPk(userId);
      if (!user) throw new ApiError("Người dùng không tồn tại", 404);

      // Nhập mật khẩu hiện tại để đổi mật khẩu
      const isPasswordValid = await bcrypt.compare(
        currentPassword,
        user.password
      );
      if (!isPasswordValid) {
        throw new ApiError("Mật khẩu hiện tại không chính xác", 400);
      }

      const hashedPassword = await bcrypt.hash(newPassword, 10);
      await user.update({ password: hashedPassword });

      return { message: "Đổi mật khẩu thành công" };
    } catch (err) {
      if (err instanceof ApiError) throw err;
      throw new ApiError("Lỗi khi đổi mật khẩu", 500);
    }
  },

  async togglePremium(userId) {
    try {
      const user = await User.findByPk(userId);
      if (!user) throw new ApiError("Người dùng không tồn tại", 404);

      await user.update({ isPremium: !user.isPremium });

      const updatedUser = await User.findByPk(userId, {
        attributes: { exclude: ["password"] },
      });

      return updatedUser.toJSON();
    } catch (err) {
      if (err instanceof ApiError) throw err;
      throw new ApiError("Lỗi khi cập nhật trạng thái premium", 500);
    }
  },

  async refreshToken(userId) {
    try {
      const user = await User.findByPk(userId);
      if (!user) throw new ApiError("Người dùng không tồn tại", 404);

      const token = this.generateToken(user);
      return { token };
    } catch (err) {
      if (err instanceof ApiError) throw err;
      throw new ApiError("Lỗi khi làm mới token", 500);
    }
  },
};

export default UserService;
