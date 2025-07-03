import Joi from 'joi';

// Định nghĩa patterns và messages cho validation
const patterns = {
  password: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d!@#$%^&*()_+\-=\[\]{};':"\\|,.<>?/~`]{8,}$/,
};

const messages = {
  'string.base': 'Phải là chuỗi',
  'string.empty': 'Không được để trống',
  'string.min': 'Phải có ít nhất {#limit} ký tự',
  'string.max': 'Không được vượt quá {#limit} ký tự',
  'string.email': 'Email không hợp lệ',
  'string.pattern.base': 'Không đúng định dạng yêu cầu',
  'any.required': 'Trường này là bắt buộc',
  'date.base': 'Phải là ngày hợp lệ',
  'date.format': 'Phải có định dạng {#format}',
  'number.base': 'Phải là số',
  'number.integer': 'Phải là số nguyên',
  'number.min': 'Không được nhỏ hơn {#limit}',
  'boolean.base': 'Phải là giá trị boolean',
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
      'string.min': 'Tên người dùng phải có ít nhất {#limit} ký tự',
      'string.max': 'Tên người dùng không được vượt quá {#limit} ký tự',
    }),
  dUserName: Joi.string()
    .trim()
    .min(3)
    .max(50)
    .messages({
      ...messages,
      'string.min': 'Tên hiển thị phải có ít nhất {#limit} ký tự',
      'string.max': 'Tên hiển thị không được vượt quá {#limit} ký tự',
    }),
  mail: Joi.string()
    .trim()
    .email()
    .required()
    .messages({
      ...messages,
      'string.email': 'Email không hợp lệ',
    }),
  password: Joi.string()
    .pattern(patterns.password)
    .required()
    .messages({
      ...messages,
      'string.pattern.base':
        'Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và có thể chứa ký tự đặc biệt',
    }),
  DOB: Joi.date()
    .iso()
    .max('now')
    .allow(null)
    .messages({
      ...messages,
      'date.max': 'Ngày sinh không thể ở tương lai',
    }),
  about: Joi.string()
    .trim()
    .max(500)
    .allow(null, '')
    .messages({
      ...messages,
      'string.max': 'Giới thiệu không được vượt quá {#limit} ký tự',
    }),
  confirmPassword: Joi.any().valid(Joi.ref('password')).required().messages({
    'any.only': 'Mật khẩu xác nhận không khớp',
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
      'string.email': 'Email không hợp lệ',
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
      'string.min': 'Tên người dùng phải có ít nhất {#limit} ký tự',
      'string.max': 'Tên người dùng không được vượt quá {#limit} ký tự',
    }),
  dUserName: Joi.string()
    .trim()
    .min(3)
    .max(50)
    .messages({
      ...messages,
      'string.min': 'Tên hiển thị phải có ít nhất {#limit} ký tự',
      'string.max': 'Tên hiển thị không được vượt quá {#limit} ký tự',
    }),
  mail: Joi.string()
    .trim()
    .email()
    .messages({
      ...messages,
      'string.email': 'Email không hợp lệ',
    }),
  DOB: Joi.date()
    .iso()
    .max('now')
    .allow(null)
    .messages({
      ...messages,
      'date.max': 'Ngày sinh không thể ở tương lai',
    }),
  about: Joi.string()
    .trim()
    .max(500)
    .allow(null, '')
    .messages({
      ...messages,
      'string.max': 'Giới thiệu không được vượt quá {#limit} ký tự',
    }),
  isPremium: Joi.boolean().messages(messages),
  roleId: Joi.number().integer().min(1).messages(messages),
}).options({ stripUnknown: true });

// Schema validation cho thay đổi mật khẩu
const changePasswordSchema = Joi.object({
  currentPassword: Joi.string()
    .required()
    .messages({
      ...messages,
      'any.required': 'Mật khẩu hiện tại là bắt buộc',
    }),
  newPassword: Joi.string()
    .pattern(patterns.password)
    .required()
    .messages({
      ...messages,
      'string.pattern.base':
        'Mật khẩu mới phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và có thể chứa ký tự đặc biệt',
    }),
  confirmPassword: Joi.string()
    .valid(Joi.ref('newPassword'))
    .required()
    .messages({
      ...messages,
      'any.only': 'Mật khẩu xác nhận không khớp',
      'any.required': 'Mật khẩu xác nhận là bắt buộc',
    }),
}).options({ stripUnknown: true });

// Schema validation cho thay đổi mật khẩu
const WalletChangeSchema = Joi.object({
  money: Joi.number()
    .integer()
    .required()
    .messages({
      ...messages,
      'number.base': 'Số tiền phải là số',
      'number.integer': 'Số tiền phải là số nguyên',
      'any.required': 'Số tiền là bắt buộc',
    }),
  type: Joi.string()
    .valid('deposit', 'withdraw')
    .required()
    .messages({
      ...messages,
      'any.only': 'Loại giao dịch phải là deposit, withdraw ',
      'any.required': 'Loại giao dịch là bắt buộc',
    }),
}).options({ stripUnknown: true });

// Schema validation cho follow/unfollow user
const followUserSchema = Joi.object({
  id: Joi.number()
    .integer()
    .min(1)
    .required()
    .messages({
      ...messages,
      'number.min': 'ID người dùng không hợp lệ',
    }),
}).options({ stripUnknown: true });

// Schema validation cho like/unlike comment
const commentSchema = Joi.object({
  id: Joi.number()
    .integer()
    .min(1)
    .required()
    .messages({
      ...messages,
      'number.min': 'ID comment không hợp lệ',
    }),
}).options({ stripUnknown: true });


// Schema validation cho refresh token
const refreshTokenSchema = Joi.object({
  refreshToken: Joi.string()
    .required()
    .messages({
      ...messages,
      'string.empty': 'Refresh token không được để trống',
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
      'number.base': 'ID người dùng phải là số',
      'number.integer': 'ID người dùng phải là số nguyên',
      'number.min': 'ID người dùng không hợp lệ',
      'any.required': 'ID người dùng là bắt buộc',
    }),
}).options({ stripUnknown: true });

export default {
  register: registerSchema,
  login: loginSchema,
  updateUser: updateUserSchema,
  changePassword: changePasswordSchema,
  followUser: followUserSchema,
  refreshToken: refreshTokenSchema,
  userId: userIdSchema,
  walletChange: WalletChangeSchema,
  comment: commentSchema,
};