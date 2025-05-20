const { User, Role, Story, Transaction } = require('../models');
const bcrypt = require('bcrypt');
import ApiError from "./../utils/apiError";

class UserService {
  async createUser(data) {
      try {
            const { password, ...userData } = data;
            const hashedPassword = await bcrypt.hash(password, 10);
            const user = await User.create({ ...userData, password: hashedPassword });
            return user.toJSON();
      }
      catch(err) {
        throw new ApiError('Lỗi khi tạo người dùng', 500);
      }
  }

  async getUserById(userId) {
    try {
        const user = await User.findByPk(userId, {
          attributes: [
            'userId',
            'userName',
            'dUserName',
            'mail',
            'about',
            'DOB',
            'followerNum',
            'avatarId',
            'backgroundId',
            'wallet',
            'isPremium',
          ],
          include: [
            { model: Role, attributes: ['roleId', 'roleName'] },
            { model: Story, attributes: ['storyId', 'storyName'] },
            { model: Transaction, attributes: ['transactionId', 'money', 'status'] },
          ],
        });
        if (!user) throw new ApiError('Người dùng không tồn tại', 404);
        return user.toJSON();
    }
    catch(err) {
        throw new ApiError('Lỗi khi lấy người dùng', 500);
    }
  }

  async getUserByEmail(mail) {
    const user = await User.findOne({
      where: { mail },
      attributes: [
        'userId',
        'userName',
        'mail',
        'password',
        'isPremium',
      ],
    });
    if (!user) throw new ApiError('Người dùng không tồn tại', 404);
    return user.toJSON();
  }

  async getAllUsers(limit = 10) {
    const users = await User.findAll({
      attributes: [
        'userId',
        'userName',
        'dUserName',
        'mail',
        'followerNum',
        'isPremium',
      ],
      include: [{ model: Role, attributes: ['roleName'] }],
      limit,
    });
    return users.map(user => user.toJSON());
  }

  async updateUser(userId, data) {
    const { password, ...updateData } = data;
    const user = await User.findByPk(userId);
    if (!user) throw new ApiError('Người dùng không tồn tại', 404);
    try {
        if (password) {
          updateData.password = await bcrypt.hash(password, 10);
        }
        await user.update(updateData);
        return user.toJSON();
    }
    catch(err) {
        throw new ApiError('Lỗi khi cập nhật người dùng', 500);
    }
  }

  async deleteUser(userId) {
    const user = await User.findByPk(userId);
    if (!user) throw new ApiError('Người dùng không tồn tại', 404);
    try {
        await user.destroy();
        return { message: 'Người dùng xoá thành công' };
    }
    catch(err) {
        throw new ApiError('Lỗi khi xoá người dùng', 500);
    }
  }
}

module.exports = new UserService();