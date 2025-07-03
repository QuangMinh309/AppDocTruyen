import { sequelize } from '../models/index.js';
import ApiError from '../utils/api_error.util.js';
import bcrypt from 'bcrypt';
import nodemailer from 'nodemailer';

const User = sequelize.models.User;
const PasswordReset = sequelize.models.PasswordReset;
const Parameter = sequelize.models.Parameter;

const PasswordResetService = {
  async sendOTP(email) {
    try {
      const user = await User.findOne({ where: { mail: email } });
      if (!user) {
        throw new ApiError('Email không tồn tại', 404);
      }

      if (user.status === 'locked') {
        throw new ApiError('Tài khoản của bạn đã bị khóa', 403);
      }

      const parameters = await Parameter.findOne();
      if (!parameters) {
        throw new ApiError('Không tìm thấy tham số hệ thống', 500);
      }

      const OTP = Math.floor(100000 + Math.random() * 900000); // 6-digit OTP
      const expiresAt = new Date(Date.now() + parameters.OTP_Valid_Period * 60 * 1000);

      const passwordReset = await PasswordReset.create({
        userId: user.userId,
        OTP,
        isUsed: false,
        createdAt: new Date(),
        expiresAt,
      });

      const transporter = nodemailer.createTransport({
        service: 'Gmail',
        auth: {
          user: process.env.EMAIL_USER || 'thiennguyen6395@gmail.com',
          pass: process.env.EMAIL_PASS || 'tprjlkeamnnxstwu',
        },
      });

      const mailOptions = {
        from: process.env.EMAIL_USER || 'thiennguyen6395@gmail.com',
        to: email,
        subject: 'Mã OTP để đặt lại mật khẩu',
        text: `Mã OTP của bạn là: ${OTP}. Mã này có hiệu lực trong ${parameters.OTP_Valid_Period} phút.`,
      };

      // console.log('Sending email to:', email);
      // console.log('Mail options:', mailOptions);
      const info = await transporter.sendMail(mailOptions);
      // console.log('Email sent:', info.response);

      return { userId: user.userId, email: user.mail };
    } catch (error) {
      console.error('Error in sendOTP:', error);
      throw error instanceof ApiError ? error : new ApiError('Lỗi khi gửi OTP', 500);
    }
  },

  async createPasswordReset(data) {
    try {
      const parameters = await Parameter.findOne();
      if (!parameters) {
        throw new ApiError('Không tìm thấy tham số hệ thống', 500);
      }

      const expiresAt = new Date(Date.now() + parameters.OTP_Valid_Period * 60 * 1000);

      const passwordReset = await PasswordReset.create({
        ...data,
        createdAt: new Date(),
        expiresAt,
        isUsed: false,
      });
      return passwordReset.toJSON();
    } catch (error) {
      throw error instanceof ApiError ? error : new ApiError('Lỗi khi tạo mã reset mật khẩu', 500);
    }
  },

  async verifyOTP(OTP, userId) {
    try {
      const passwordReset = await PasswordReset.findOne({
        where: { OTP, userId, isUsed: false },
        include: [{ model: User, as: 'user', attributes: ['userId', 'mail', 'status'] }],
      });

      if (!passwordReset) {
        throw new ApiError('Mã OTP không hợp lệ hoặc đã được sử dụng', 400);
      }

      if (new Date() > passwordReset.expiresAt) {
        throw new ApiError('Mã OTP đã hết hạn', 400);
      }

      if (passwordReset.user.status === 'locked') {
        throw new ApiError('Tài khoản của bạn đã bị khóa', 403);
      }

      return passwordReset.toJSON();
    } catch (error) {
      console.error('Error in verifyOTP:', error);
      throw error instanceof ApiError ? error : new ApiError('Lỗi khi xác minh OTP', 500);
    }
  },

  async getPasswordResetById(OTP) {
    try {
      const passwordReset = await PasswordReset.findOne({
        where: { OTP },
        include: [{ model: User, as: 'user', attributes: ['userId', 'mail', 'status'] }],
      });

      if (!passwordReset) {
        throw new ApiError('Mã OTP không tồn tại', 404);
      }

      if (passwordReset.isUsed) {
        throw new ApiError('Mã OTP đã được sử dụng', 400);
      }

      if (new Date() > passwordReset.expiresAt) {
        throw new ApiError('Mã OTP đã hết hạn', 400);
      }

      return passwordReset.toJSON();
    } catch (error) {
      throw error instanceof ApiError ? error : new ApiError('Lỗi khi lấy mã OTP', 500);
    }
  },

  async resetPassword(otp, newPassword, userId) { // Loại bỏ confirmPassword khỏi tham số
    const transaction = await sequelize.transaction();
    try {
      const passwordReset = await PasswordReset.findOne({
        where: { OTP: otp, userId, isUsed: false },
        include: [{ model: User, as: 'user', attributes: ['userId', 'mail', 'status'] }],
      });

      if (!passwordReset) {
        throw new ApiError('Mã OTP không hợp lệ hoặc đã được sử dụng', 400);
      }

      if (new Date() > passwordReset.expiresAt) {
        throw new ApiError('Mã OTP đã hết hạn', 400);
      }

      if (passwordReset.user.status === 'locked') {
        throw new ApiError('Tài khoản của bạn đã bị khóa', 403);
      }

      const hashedPassword = await bcrypt.hash(newPassword, 10);
      await User.update(
        { password: hashedPassword },
        { where: { userId: passwordReset.userId }, transaction }
      );

      await passwordReset.update({ isUsed: true }, { transaction });

      await transaction.commit();
      return { userId: passwordReset.userId };
    } catch (error) {
      await transaction.rollback();
      throw error instanceof ApiError ? error : new ApiError('Lỗi khi đặt lại mật khẩu', 500);
    }
  }
};

export default PasswordResetService;