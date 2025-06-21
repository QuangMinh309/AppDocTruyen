import Joi from 'joi';

const categoryValidation = {
  create: Joi.object({
    categoryName: Joi.string().max(255).required().messages({
      'string.empty': 'Tên thể loại không được để trống.',
      'string.max': 'Tên thể loại không được vượt quá 255 ký tự.',
      'any.required': 'Tên thể loại là bắt buộc.',
    }),
  }),
  update: Joi.object({
    categoryName: Joi.string().max(255).required().messages({
      'string.empty': 'Tên thể loại không được để trống.',
      'string.max': 'Tên thể loại không được vượt quá 255 ký tự.',
      'any.required': 'Tên thể loại là bắt buộc.',
    }),
  }),
  idSchema: Joi.object({
    categoryId: Joi.number().integer().min(1).required().messages({
      'number.min': 'ID phải là số nguyên dương',
      'any.required': 'ID là bắt buộc',
    }),
  }),
};

export default categoryValidation;
