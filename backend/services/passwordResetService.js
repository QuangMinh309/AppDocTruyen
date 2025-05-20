const { PasswordReset, User } = require('../models');
import ApiError from "./../utils/apiError";

class PasswordResetService {
  async createPasswordReset(data) {
    try {
        const passwordReset = await PasswordReset.create(data);
        return passwordReset.toJSON();
    }
    catch {
        throw new ApiError('Lỗi khi tạo mã reset mật khẩu', 500);
    }
  }

  async verifyOTP(OTP, userId) {
    const passwordReset = await PasswordReset.findOne({
      where: { OTP, userId, isUsed: false },
      include: [{ model: User, attributes: ['userId', 'mail'] }],
    });
    if (!passwordReset) throw new ApiError('Mã OTP đã được dùng hoặc không hợp lệ', 400);
    try {
        await passwordReset.update({ isUsed: true });
        return passwordReset.toJSON();
    }
    catch(err) {
        throw new ApiError('Lỗi khi cập nhật mã reset mật khẩu', 500);
    }
  }

  async getPasswordResetById(OTP) {
    try {
        const passwordReset = await PasswordReset.findByPk(OTP);
        if (!passwordReset) throw new ApiError('Mã OTP không tồn tại', 404);
        return passwordReset.toJSON();
    }
    catch(err) {
        throw new ApiError('Lỗi khi lấy mã OTP', 500);
    }
  }
}

module.exports = new PasswordResetService();