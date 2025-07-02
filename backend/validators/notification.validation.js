import Joi from 'joi'

const notificationValidation = {
  create: Joi.object({
    type: Joi.string().required().max(255).messages({
      'string.empty': 'Loại thông báo không được để trống.',
      'string.max': 'Loại thông báo không được vượt quá 255 ký tự.',
      'any.required': 'Loại thông báo là bắt buộc.',
    }),
    content: Joi.string().required().messages({
      'string.empty': 'Nội dung không được để trống.',
      'any.required': 'Nội dung là bắt buộc.',
    }),
    refId: Joi.number().integer().min(1).required().messages({
      'number.min': 'ID phải là số nguyên không âm',
      'any.required': 'ID là bắt buộc.',
    }),
    status: Joi.string().optional().max(255).messages({
      'string.max': 'Trạng thái không được vượt quá 255 ký tự.',
      'string.base': 'Trạng thái phải là chuỗi.',
    }),
  }),
  update: Joi.object({
    status: Joi.string().optional().max(255).messages({
      'string.max': 'Trạng thái không được vượt quá 255 ký tự.',
      'string.base': 'Trạng thái phải là chuỗi.',
    }),
  }),
  idSchema: Joi.object({
    id: Joi.number().integer().min(1).required().messages({
      'number.min': 'ID phải là số nguyên dương',
      'any.required': 'ID là bắt buộc',
    }),
  }),
}

export default notificationValidation
