import Joi from "joi";

const userValidation = {
  create: Joi.object({
    userName: Joi.string().required().max(255).messages({
        "string.empty": "Tên người dùng không được để trống.",
        "string.max": "Tên người dùng không được vượt quá 255 ký tự.",
        "any.required": "Tên người dùng là bắt buộc.",
    }),
    roleId: Joi.number().integer().min(1).required().messages({
        "number.min": "ID vai trò phải là số nguyên dương",
        "any.required": "ID vai trò là bắt buộc",
    }),
    dUserName: Joi.string().optional().max(255).messages({
         "string.max": "Tên ngắn không được vượt quá 255 ký tự.",
         "string.base": "Tên ngắn phải là chuỗi.",
    }),
    mail: Joi.string().email().required().max(255).messages({
        "string.empty": "Địa chỉ email không được để trống.",
        "string.max": "Địa chỉ email không được vượt quá 255 ký tự.",
        "any.required": "Địa chỉ email là bắt buộc.",
    }),
    about: Joi.string().optional().max(1500).messages({
         "string.max": "Giới thiệu không được vượt quá 1500 ký tự.",
         "string.base": "Giới thiệu phải là chuỗi.",
    }),
    DOB: Joi.date().optional().messages({

    }),
    followerNum: Joi.number().integer().default(0),
    password: Joi.string().required().min(6).messages({
        "string.empty": "Mật khầu không được để trống.",
        "string.max": "Mật khẩu tối thiểu phải là 6 ký tự.",
        "any.required": "Mật khầu là bắt buộc.",
    }),
    avatarId: Joi.string().optional().messages({
        "string.base": "ID ảnh đại diện phải là chuỗi.",
    }),
    backgroundId: Joi.string().optional().messages({
        "string.base": "ID hình nền phải là chuỗi.",
    }),
    wallet: Joi.number().precision(2).default(0.00),
    isPremium: Joi.boolean().default(false),
  }),
  update: Joi.object({
    userName: Joi.string().required().max(255).messages({
        "string.empty": "Tên người dùng không được để trống.",
        "string.max": "Tên người dùng không được vượt quá 255 ký tự.",
        "any.required": "Tên người dùng là bắt buộc.",
    }),
    dUserName: Joi.string().optional().max(255).messages({
        "string.max": "Tên ngắn không được vượt quá 255 ký tự.",
        "string.base": "Tên ngắn phải là chuỗi.",
    }),
    mail: Joi.string().email().optional().max(255).messages({
        "string.max": "Địa chỉ email không được vượt quá 255 ký tự.",
    }),
    about: Joi.string().optional().max(1500).messages({
        "string.max": "Giới thiệu không được vượt quá 1500 ký tự.",
        "string.base": "Giới thiệu phải là chuỗi.",
    }),
    DOB: Joi.date().optional().messages({

    }),
    avatarId: Joi.string().optional().messages({
        "string.base": "ID ảnh đại diện phải là chuỗi.",
    }),
    backgroundId: Joi.string().optional().messages({
        "string.base": "ID hình nền phải là chuỗi.",
    }),
    isPremium: Joi.boolean().optional().messages({
        "boolean.base": "Trạng thái Premium phải là boolean.",
    }),
  }),
  idSchema: Joi.object({
      id: Joi.number().integer().min(1).required().messages({
        "number.min": "ID phải là số nguyên dương",
        "any.required": "ID là bắt buộc",
      }),
  }),
};

export default userValidation;