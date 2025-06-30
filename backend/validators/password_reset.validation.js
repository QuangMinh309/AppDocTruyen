import Joi from 'joi';

const passwordResetValidation = {
  sendOTP: Joi.object({
    email: Joi.string().email().required().messages({
      'string.email': 'Email không hợp lệ',
      'any.required': 'Email là bắt buộc',
    }),
  }),
  create: Joi.object({
    userId: Joi.number().integer().min(1).required().messages({
      'number.min': 'ID người dùng phải là số nguyên dương',
      'any.required': 'ID người dùng là bắt buộc',
    }),
    isUsed: Joi.boolean().default(false),
  }),
  verify: Joi.object({
    OTP: Joi.number().integer().min(1).required().messages({
      'number.min': 'OTP phải là số nguyên dương',
      'any.required': 'OTP là bắt buộc',
    }),
    userId: Joi.number().integer().min(1).required().messages({
      'number.min': 'ID người dùng phải là số nguyên dương',
      'any.required': 'ID người dùng là bắt buộc',
    }),
  }),
  idSchema: Joi.object({
    id: Joi.number().integer().min(1).required().messages({
      'number.min': 'ID phải là số nguyên dương',
      'any.required': 'ID là bắt buộc',
    }),
  }),
  reset: Joi.object({
    otp: Joi.number().integer().min(100000).max(999999).required().messages({
      'number.min': 'OTP phải là số 6 chữ số',
      'number.max': 'OTP phải là số 6 chữ số',
      'any.required': 'OTP là bắt buộc',
    }),
    newPassword: Joi.string()
      .pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d!@#$%^&*(),.?":{}|<>]{8,}$/)
      .required()
      .messages({
        'string.pattern.base': 'Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số',
        'any.required': 'Mật khẩu mới là bắt buộc',
      }),
    confirmPassword: Joi.string()
      .valid(Joi.ref('newPassword'))
      .required()
      .messages({
        'any.only': 'Mật khẩu xác nhận không khớp',
        'any.required': 'Xác nhận mật khẩu là bắt buộc',
      }),
    userId: Joi.number().integer().min(1).required().messages({
      'number.min': 'ID người dùng phải là số nguyên dương',
      'any.required': 'ID người dùng là bắt buộc',
    }),
  }),
};

export default passwordResetValidation;