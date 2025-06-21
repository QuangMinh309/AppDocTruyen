import Joi from 'joi';

const NameListValidation = {
  createReadingList: Joi.object({
    nameList: Joi.string().required().max(100).messages({
      'string.base': 'Tên danh sách phải là chuỗi',
      'string.empty': 'Tên danh sách không được để trống',
      'string.max': 'Tên danh sách không được vượt quá 100 ký tự',
      'any.required': 'Tên danh sách là bắt buộc',
    }),
    description: Joi.string().allow('').max(500).messages({
      'string.base': 'Mô tả phải là chuỗi',
      'string.max': 'Mô tả không được vượt quá 500 ký tự',
    }),
    stories: Joi.array()
      .items(Joi.number().integer().positive())
      .optional()
      .messages({
        'array.base': 'Danh sách truyện phải là mảng',
        'number.base': 'ID truyện phải là số nguyên dương',
      }),
  }),

  updateReadingList: Joi.object({
    nameList: Joi.string().max(100).optional().messages({
      'string.base': 'Tên danh sách phải là chuỗi',
      'string.max': 'Tên danh sách không được vượt quá 100 ký tự',
    }),
    description: Joi.string().allow('').max(500).optional().messages({
      'string.base': 'Mô tả phải là chuỗi',
      'string.max': 'Mô tả không được vượt quá 500 ký tự',
    }),
  }),

  addToReadingList: Joi.object({
    storyId: Joi.number().integer().positive().required().messages({
      'number.base': 'ID truyện phải là số nguyên dương',
      'any.required': 'ID truyện là bắt buộc',
    }),
  }),

  getReadingList: Joi.object({
    limit: Joi.number()
      .integer()
      .positive()
      .max(100)
      .default(20)
      .optional()
      .messages({
        'number.base': 'Giới hạn phải là số nguyên dương',
        'number.max': 'Giới hạn không được vượt quá 100',
      }),
    lastId: Joi.number().integer().positive().optional().messages({
      'number.base': 'lastId phải là số nguyên dương',
    }),
  }),

  getUserReadingLists: Joi.object({
    limit: Joi.number()
      .integer()
      .positive()
      .max(100)
      .default(20)
      .optional()
      .messages({
        'number.base': 'Giới hạn phải là số nguyên dương',
        'number.max': 'Giới hạn không được vượt quá 100',
      }),
    lastId: Joi.number().integer().positive().optional().messages({
      'number.base': 'lastId phải là số nguyên dương',
    }),
  }),
};

export default NameListValidation;
