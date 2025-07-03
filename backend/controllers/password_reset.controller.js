import db from '../models/index.js';
import ApiError from '../utils/api_error.util.js';
import bcrypt from 'bcrypt';
import nodemailer from 'nodemailer';
import dotenv from 'dotenv';
dotenv.config();

import PasswordResetService from '../services/password_reset.service.js'; // Sửa đường dẫn

async function createPasswordReset(req, res, next) {
  try {
    const { userId, isUsed = false } = req.body;
    const result = await PasswordResetService.createPasswordReset({ userId, isUsed });
    res.status(201).json({
      success: true,
      message: 'Tạo mã reset mật khẩu thành công',
      data: result,
    });
  } catch (error) {
    next(error);
  }
}

async function sendOTP(req, res, next) {
  try {
    const { email } = req.body;
    const result = await PasswordResetService.sendOTP(email);
    res.status(200).json({
      success: true,
      message: 'OTP đã được gửi đến email của bạn',
      data: result,
    });
  } catch (error) {
    next(error);
  }
}

async function verifyOTP(req, res, next) {
  try {
    const { OTP, userId } = req.body;
    const result = await PasswordResetService.verifyOTP(OTP, userId);
    res.status(200).json({
      success: true,
      message: 'Xác minh OTP thành công',
      data: result,
    });
  } catch (error) {
    next(error);
  }
}

async function getPasswordResetById(req, res, next) {
  try {
    const { OTP } = req.params;
    const result = await PasswordResetService.getPasswordResetById(OTP);
    res.status(200).json({
      success: true,
      message: 'Lấy thông tin mã OTP thành công',
      data: result,
    });
  } catch (error) {
    next(error);
  }
}

async function resetPassword(req, res, next) {
  try {
    const { otp, newPassword, confirmPassword, userId } = req.body;
    // console.log('Received newPassword:', newPassword);
    // console.log('Received confirmPassword:', confirmPassword);
    if (newPassword !== confirmPassword) {
      return next(new ApiError('Mật khẩu mới và mật khẩu xác nhận không khớp', 400));
    }

    const result = await PasswordResetService.resetPassword(otp, newPassword, userId);
    res.status(200).json({
      success: true,
      message: 'Đặt lại mật khẩu thành công',
      data: result,
    });
  } catch (error) {
    next(error);
  }
}

export default {
  createPasswordReset,
  sendOTP,
  verifyOTP,
  getPasswordResetById,
  resetPassword,
};