import Joi from "joi";

const ChapterValidation = {
  chapterCreateSchema: Joi.object({
    chapterName: Joi.string().required().max(255).messages({
      "string.empty": "Tên chương không được để trống",
      "string.max": "Tên chương không được vượt quá 255 ký tự",
      "any.required": "Tên chương là bắt buộc",
    }),
    ordinalNumber: Joi.number().integer().min(1).required().messages({
      "number.min": "Số thứ tự chương phải là số nguyên dương",
      "any.required": "Số thứ tự chương là bắt buộc",
    }),
    storyId: Joi.number().integer().min(1).required().messages({
      "number.min": "ID truyện phải là số nguyên dương",
      "any.required": "ID truyện là bắt buộc",
    }),
    content: Joi.string().allow("").optional().messages({
      "string.base": "Nội dung phải là chuỗi",
    }),
    lockedStatus: Joi.boolean().optional().messages({
      "boolean.base": "Trạng thái khóa phải là boolean",
    }),
  }),

  chapterUpdateSchema: Joi.object({
    chapterId: Joi.number().integer().min(1).required().messages({
      "number.min": "ID chương phải là số nguyên dương",
      "any.required": "ID chương là bắt buộc",
    }),
    chapterName: Joi.string().required().max(255).messages({
      "string.empty": "Tên chương không được để trống",
      "string.max": "Tên chương không được vượt quá 255 ký tự",
      "any.required": "Tên chương là bắt buộc",
    }),
    ordinalNumber: Joi.number().integer().min(1).required().messages({
      "number.min": "Số thứ tự chương phải là số nguyên dương",
      "any.required": "Số thứ tự chương là bắt buộc",
    }),
    storyId: Joi.number().integer().min(1).required().messages({
      "number.min": "ID truyện phải là số nguyên dương",
      "any.required": "ID truyện là bắt buộc",
    }),
    content: Joi.string().allow("").optional().messages({
      "string.base": "Nội dung phải là chuỗi",
    }),
    lockedStatus: Joi.boolean().optional().messages({
      "boolean.base": "Trạng thái khóa phải là boolean",
    }),
  }),

  idSchema: Joi.object({
    id: Joi.number().integer().min(1).required().messages({
      "number.min": "ID phải là số nguyên dương",
      "any.required": "ID là bắt buộc",
    }),
  }),
};

export default ChapterValidation;
