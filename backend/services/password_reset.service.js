import db from '../models/index.js';
import ApiError from '../utils/api_error.util.js';
import bcrypt from 'bcrypt';
import nodemailer from 'nodemailer';
import dotenv from 'dotenv';
dotenv.config();

const PasswordResetService = {
  async sendOTP(email) {
    try {
      const user = await db.User.findOne({ where: { mail: email } });
      if (!user) {
        throw new ApiError('Email không tồn tại', 404);
      }

      const OTP = Math.floor(100000 + Math.random() * 900000);

      const passwordReset = await db.PasswordReset.create({
        userId: user.userId,
        OTP: OTP,
        isUsed: false,
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
        text: `Mã OTP của bạn là: ${OTP}. Mã này có hiệu lực trong 10 phút.`,
      };

      console.log('Sending email to:', email);
      console.log('Mail options:', mailOptions);
      const info = await transporter.sendMail(mailOptions);
      console.log('Email sent:', info.response);

      return { userId: user.userId, email: user.mail };
    } catch (error) {
      console.error('Error in sendOTP:', error);
      throw error instanceof ApiError ? error : new ApiError('Lỗi khi gửi OTP: ' + error.message, 500);
    }
  },

  async createPasswordReset(data) {
    try {
      const passwordReset = await db.PasswordReset.create({
        ...data,
        createdAt: new Date(),
      });
      return passwordReset.toJSON();
    } catch (error) {
      throw new ApiError('Lỗi khi tạo mã reset mật khẩu', 500);
    }
  },

  async verifyOTP(OTP, userId) {
    try {
      console.log('Verifying OTP:', { OTP, userId });
      const passwordReset = await db.PasswordReset.findOne({
        where: { OTP, userId, isUsed: false },
        include: [{ model: db.User, attributes: ['userId', 'mail'], as: 'user' }],
      });
      console.log('Found password reset:', passwordReset ? passwordReset.toJSON() : null);
      if (!passwordReset) throw new ApiError('OTP không đúng hoặc đã hết hạn', 400);

      return passwordReset.toJSON();
    } catch (err) {
      console.error('Error in verifyOTP:', err);
      throw new ApiError('Lỗi khi xác minh OTP', 500);
    }
  },

  async getPasswordResetById(OTP) {
    try {
      const passwordReset = await db.PasswordReset.findByPk(OTP);
      if (!passwordReset) throw new ApiError('Mã OTP không tồn tại', 404);
      return passwordReset.toJSON();
    } catch (err) {
      throw new ApiError('Lỗi khi lấy mã OTP', 500);
    }
  },

  async resetPassword(otp, newPassword, userId) {
    const transaction = await db.sequelize.transaction();
    try {
      const passwordReset = await db.PasswordReset.findOne({
        where: { OTP: otp, userId, isUsed: false },
        include: [{ model: db.User, attributes: ['userId', 'mail', 'password'], as: 'user' }],
      });

      if (!passwordReset) throw new ApiError('Mã OTP không hợp lệ hoặc đã được sử dụng', 400);

      const hashedPassword = await bcrypt.hash(newPassword, 10);
      await db.User.update(
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
  },
};

export default PasswordResetService;