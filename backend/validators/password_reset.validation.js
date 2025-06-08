import Joi from 'joi'

const passwordResetValidation = {
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
}

export default passwordResetValidation
