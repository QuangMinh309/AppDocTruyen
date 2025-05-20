import Joi from "joi";

const StoryValidation = {
  storyCreateSchema: Joi.object({
    storyName: Joi.string().required().max(255).messages({
      "string.empty": "Tên truyện không được để trống.",
      "string.max": "Tên truyện không được vượt quá 255 ký tự.",
      "any.required": "Tên truyện là bắt buộc.",
    }),
    description: Joi.string().max(1500).allow("").optional().messages({
      "string.max": "Mô tả không được vượt quá 1500 ký tự",
    }),
    ageRange: Joi.number().integer().min(0).optional().messages({
      "number.min": "Độ tuổi phải là số nguyên không âm",
    }),
    status: Joi.string().optional().messages({
      "string.base": "Trạng thái phải là chuỗi",
    }),
    price: Joi.number().min(0).optional().messages({
      "number.min": "Giá phải là số thực không âm",
    }),
    pricePerChapter: Joi.number().min(0).optional().messages({
      "number.min": "Giá mỗi chương phải là số thực không âm",
    }),
  }),

  storyUpdateSchema: Joi.object({
    storyId: Joi.number().integer().min(1).required().messages({
      "number.min": "ID truyện phải là số nguyên dương",
      "any.required": "ID truyện là bắt buộc",
    }),
    storyName: Joi.string().required().max(255).messages({
      "string.empty": "Tên truyện không được để trống.",
      "string.max": "Tên truyện không được vượt quá 255 ký tự.",
      "any.required": "Tên truyện là bắt buộc.",
    }),
    description: Joi.string().max(1500).allow("").optional().messages({
      "string.max": "Mô tả không được vượt quá 1500 ký tự",
    }),
    ageRange: Joi.number().integer().min(0).optional().messages({
      "number.min": "Độ tuổi phải là số nguyên không âm",
    }),
    status: Joi.string().optional().messages({
      "string.base": "Trạng thái phải là chuỗi",
    }),
    price: Joi.number().min(0).optional().messages({
      "number.min": "Giá phải là số thực không âm",
    }),
    pricePerChapter: Joi.number().min(0).optional().messages({
      "number.min": "Giá mỗi chương phải là số thực không âm",
    }),
  }),

  idSchema: Joi.object({
    id: Joi.number().integer().min(1).required().messages({
      "number.min": "ID phải là số nguyên dương",
      "any.required": "ID là bắt buộc",
    }),
  }),
};

export default StoryValidation;
