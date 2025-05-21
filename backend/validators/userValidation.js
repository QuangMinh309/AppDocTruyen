import Joi from "joi";

// Định nghĩa patterns và messages cho validation
const patterns = {
  password: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/,
};

const messages = {
  "string.base": "Phải là chuỗi",
  "string.empty": "Không được để trống",
  "string.min": "Phải có ít nhất {#limit} ký tự",
  "string.max": "Không được vượt quá {#limit} ký tự",
  "string.email": "Email không hợp lệ",
  "string.pattern.base": "Không đúng định dạng yêu cầu",
  "any.required": "Trường này là bắt buộc",
  "date.base": "Phải là ngày hợp lệ",
  "date.format": "Phải có định dạng {#format}",
  "number.base": "Phải là số",
  "number.integer": "Phải là số nguyên",
  "number.min": "Không được nhỏ hơn {#limit}",
};

// Schema validation cho đăng ký
const registerSchema = Joi.object({
  userName: Joi.string()
    .trim()
    .min(3)
    .max(50)
    .required()
    .messages({
      ...messages,
      "string.min": "Tên người dùng phải có ít nhất {#limit} ký tự",
      "string.max": "Tên người dùng không được vượt quá {#limit} ký tự",
    }),
  dUserName: Joi.string()
    .trim()
    .min(3)
    .max(50)
    .messages({
      ...messages,
      "string.min": "Tên hiển thị phải có ít nhất {#limit} ký tự",
      "string.max": "Tên hiển thị không được vượt quá {#limit} ký tự",
    }),
  mail: Joi.string()
    .trim()
    .email()
    .required()
    .messages({
      ...messages,
      "string.email": "Email không hợp lệ",
    }),
  password: Joi.string()
    .pattern(patterns.password)
    .required()
    .messages({
      ...messages,
      "string.pattern.base":
        "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số",
    }),
  DOB: Joi.date()
    .iso()
    .max("now")
    .allow(null)
    .messages({
      ...messages,
      "date.max": "Ngày sinh không thể ở tương lai",
    }),
  about: Joi.string()
    .trim()
    .max(500)
    .allow(null, "")
    .messages({
      ...messages,
      "string.max": "Giới thiệu không được vượt quá {#limit} ký tự",
    }),
}).options({ stripUnknown: true });

// Schema validation cho đăng nhập
const loginSchema = Joi.object({
  mail: Joi.string()
    .trim()
    .email()
    .required()
    .messages({
      ...messages,
      "string.email": "Email không hợp lệ",
    }),
  password: Joi.string().required().messages(messages),
}).options({ stripUnknown: true });

// Schema validation cho cập nhật người dùng
const updateUserSchema = Joi.object({
  userName: Joi.string()
    .trim()
    .min(3)
    .max(50)
    .messages({
      ...messages,
      "string.min": "Tên người dùng phải có ít nhất {#limit} ký tự",
      "string.max": "Tên người dùng không được vượt quá {#limit} ký tự",
    }),
  dUserName: Joi.string()
    .trim()
    .min(3)
    .max(50)
    .messages({
      ...messages,
      "string.min": "Tên hiển thị phải có ít nhất {#limit} ký tự",
      "string.max": "Tên hiển thị không được vượt quá {#limit} ký tự",
    }),
  mail: Joi.string()
    .trim()
    .email()
    .messages({
      ...messages,
      "string.email": "Email không hợp lệ",
    }),
  DOB: Joi.date()
    .iso()
    .max("now")
    .allow(null)
    .messages({
      ...messages,
      "date.max": "Ngày sinh không thể ở tương lai",
    }),
  about: Joi.string()
    .trim()
    .max(500)
    .allow(null, "")
    .messages({
      ...messages,
      "string.max": "Giới thiệu không được vượt quá {#limit} ký tự",
    }),
  avatarId: Joi.number().integer().min(1).allow(null).messages(messages),
  backgroundId: Joi.number().integer().min(1).allow(null).messages(messages),
  isPremium: Joi.boolean().messages(messages),
}).options({ stripUnknown: true });

// Schema validation cho thay đổi mật khẩu
const changePasswordSchema = Joi.object({
  currentPassword: Joi.string()
    .required()
    .messages({
      ...messages,
      "any.required": "Mật khẩu hiện tại là bắt buộc",
    }),
  newPassword: Joi.string()
    .pattern(patterns.password)
    .required()
    .messages({
      ...messages,
      "string.pattern.base":
        "Mật khẩu mới phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số",
    }),
  confirmPassword: Joi.string()
    .valid(Joi.ref("newPassword"))
    .required()
    .messages({
      ...messages,
      "any.only": "Mật khẩu xác nhận không khớp",
      "any.required": "Mật khẩu xác nhận là bắt buộc",
    }),
}).options({ stripUnknown: true });

// Schema validation cho phân trang
const paginationSchema = Joi.object({
  page: Joi.number()
    .integer()
    .min(1)
    .default(1)
    .messages({
      ...messages,
      "number.min": "Số trang không được nhỏ hơn {#limit}",
    }),
  limit: Joi.number()
    .integer()
    .min(1)
    .max(100)
    .default(10)
    .messages({
      ...messages,
      "number.min": "Số lượng không được nhỏ hơn {#limit}",
      "number.max": "Số lượng không được lớn hơn {#limit}",
    }),
}).options({ stripUnknown: true });

// Schema validation cho userId trong params
const userIdSchema = Joi.object({
  userId: Joi.number()
    .integer()
    .min(1)
    .required()
    .messages({
      ...messages,
      "number.base": "ID người dùng phải là số",
      "number.integer": "ID người dùng phải là số nguyên",
      "number.min": "ID người dùng không hợp lệ",
      "any.required": "ID người dùng là bắt buộc",
    }),
}).options({ stripUnknown: true });

export default {
  register: registerSchema,
  login: loginSchema,
  updateUser: updateUserSchema,
  changePassword: changePasswordSchema,
  pagination: paginationSchema,
  userId: userIdSchema,
};
