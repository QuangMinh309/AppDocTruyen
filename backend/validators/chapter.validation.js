import Joi from 'joi'

export const validateCreateChapter = Joi.object({
  chapterName: Joi.string().trim().max(255).required().messages({
    'string.empty': 'Tên chương không được để trống',
    'string.max': 'Tên chương không được vượt quá 255 ký tự',
    'any.required': 'Tên chương là bắt buộc',
  }),
  content: Joi.string().trim().required().messages({
    'string.empty': 'Nội dung chương không được để trống',
    'any.required': 'Nội dung chương là bắt buộc',
  }),
  ordinalNumber: Joi.number().integer().min(1).optional().messages({
    'number.base': 'Số thứ tự chương phải là số',
    'number.integer': 'Số thứ tự chương phải là số nguyên',
    'number.min': 'Số thứ tự chương phải lớn hơn hoặc bằng 1',
  }),
  lockedStatus: Joi.boolean().optional().messages({
    'boolean.base': 'Trạng thái khóa phải là true hoặc false',
  }),
})

export const validateStoryIdParam = Joi.object({
  storyId: Joi.number().integer().positive().required().messages({
    'number.base': 'ID truyện phải là số',
    'number.integer': 'ID truyện phải là số nguyên',
    'number.positive': 'ID truyện phải là số dương',
    'any.required': 'ID truyện là bắt buộc',
  }),
})

export const validateUpdateChapter = Joi.object({
  chapterName: Joi.string().trim().max(255).optional().messages({
    'string.empty': 'Tên chương không được để trống',
    'string.max': 'Tên chương không được vượt quá 255 ký tự',
  }),
  content: Joi.string().trim().optional().messages({
    'string.empty': 'Nội dung chương không được để trống',
  }),
  ordinalNumber: Joi.number().integer().min(1).optional().messages({
    'number.base': 'Số thứ tự chương phải là số',
    'number.integer': 'Số thứ tự chương phải là số nguyên',
    'number.min': 'Số thứ tự chương phải lớn hơn hoặc bằng 1',
  }),
  lockedStatus: Joi.boolean().optional().messages({
    'boolean.base': 'Trạng thái khóa phải là true hoặc false',
  }),
})
  .min(1)
  .messages({
    'object.min': 'Phải có ít nhất một trường để cập nhật',
  })

export const validatePurchaseChapter = Joi.object({
  chapterId: Joi.number().integer().positive().required().messages({
    'number.base': 'ID chương phải là số',
    'number.integer': 'ID chương phải là số nguyên',
    'number.positive': 'ID chương phải là số dương',
    'any.required': 'ID chương là bắt buộc',
  }),
})

export const validateGetStories = Joi.object({
  storyId: Joi.number().integer().positive().required().messages({
    'number.base': 'ID truyện phải là số',
    'number.integer': 'ID truyện phải là số nguyên',
    'number.positive': 'ID truyện phải là số dương',
    'any.required': 'ID truyện là bắt buộc',
  }),
  limit: Joi.number().integer().min(1).max(100).default(20).messages({
    'number.base': 'Giới hạn phải là số',
    'number.integer': 'Giới hạn phải là số nguyên',
    'number.min': 'Giới hạn phải lớn hơn hoặc bằng 1',
    'number.max': 'Giới hạn không được vượt quá 100',
  }),
  lastId: Joi.number().integer().positive().allow(null).default(null).messages({
    'number.base': 'LastId phải là số',
    'number.integer': 'LastId phải là số nguyên',
    'number.positive': 'LastId phải là số dương',
  }),
  orderBy: Joi.string()
    .valid('ordinalNumber', 'createdAt', 'updatedAt')
    .default('ordinalNumber')
    .messages({
      'string.base': 'OrderBy phải là chuỗi',
      'any.only':
        "OrderBy chỉ có thể là 'ordinalNumber', 'createdAt', hoặc 'updatedAt'",
    }),
  sort: Joi.string().valid('ASC', 'DESC').default('ASC').messages({
    'string.base': 'Sort phải là chuỗi',
    'any.only': "Sort chỉ có thể là 'ASC' hoặc 'DESC'",
  }),
})

export const readChapterSchema = Joi.object({
  chapterId: Joi.number().integer().positive().required().messages({
    'number.base': 'ID chương phải là số',
    'number.integer': 'ID chương phải là số nguyên',
    'number.positive': 'ID chương phải là số dương',
    'any.required': 'ID chương là bắt buộc',
  }),
})

export const validateChapterId = Joi.object({
  chapterId: Joi.number().integer().positive().required().messages({
    'number.base': 'ID chương phải là số',
    'number.integer': 'ID chương phải là số nguyên',
    'number.positive': 'ID chương phải là số dương',
    'any.required': 'ID chương là bắt buộc',
  }),
})

export const validateStoryChapterIdParam = Joi.object({
  storyId: Joi.number().integer().positive().required(),
  chapterId: Joi.number().integer().positive().required(),
})