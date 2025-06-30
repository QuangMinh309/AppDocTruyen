import Joi from 'joi';
import { validateUser } from '../utils/user.util.js';

const messages = {
  'string.base': 'Phải là chuỗi',
  'string.empty': 'Không được để trống',
  'any.required': 'Trường này là bắt buộc',
  'date.base': 'Phải là ngày hợp lệ',
  'number.base': 'Phải là số',
  'number.integer': 'Phải là số nguyên',
};
// Schema validation cho tạo/cập nhật giao dịch
const transactionUpdateSchema = Joi.object({
  money: Joi.number()
    .integer()
    .messages({
      ...messages,
      'number.base': 'Số tiền phải là số',
      'number.integer': 'Số tiền phải là số nguyên',
      'any.required': 'Số tiền là bắt buộc',
    }),
  type: Joi.string()
    .valid('deposit', 'withdraw', 'purchase')
    .messages({
      ...messages,
      'any.only': 'Loại giao dịch phải là deposit, withdraw hoặc purchase',
      'any.required': 'Loại giao dịch là bắt buộc',
    }),
  status: Joi.string()
    .valid('pending', 'success')
    .messages({
      ...messages,
      'any.only': 'Trạng thái phải là pending hoặc success',
      'any.required': 'Trạng thái là bắt buộc',
    })
}).options({ stripUnknown: true });

// Schema validation cho tạo/cập nhật giao dịch
const transactionSchema = Joi.object({
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
    .valid('deposit', 'withdraw', 'purchase')
    .required()
    .messages({
      ...messages,
      'any.only': 'Loại giao dịch phải là deposit, withdraw hoặc purchase',
      'any.required': 'Loại giao dịch là bắt buộc',
    }),
  status: Joi.string()
    .valid('pending', 'success')
    .required()
    .messages({
      ...messages,
      'any.only': 'Trạng thái phải là pending hoặc success',
      'any.required': 'Trạng thái là bắt buộc',
    }),
  createdAt: Joi.date()
    .iso()
    .allow(null)
    .messages({
      ...messages,
      'date.base': 'Ngày tạo phải là định dạng ngày hợp lệ',
    }),
  time: Joi.date()
    .iso()
    .allow(null)
    .messages({
      ...messages,
      'date.base': 'Thời gian giao dịch phải là định dạng ngày hợp lệ',
    }),
  finishAt: Joi.date()
    .iso()
    .allow(null)
    .messages({
      ...messages,
      'date.base': 'Thời gian hoàn thành phải là định dạng ngày hợp lệ',
    }),
  bankAccountData: Joi.object({
    accountNumber: Joi.string() // Hoặc Joi.number() nếu muốn số
      .allow(null)
      .optional()
      .messages({
        ...messages,
        'string.base': 'Số tài khoản phải là chuỗi',
      }),
    accountHolder: Joi.string()
      .allow(null)
      .optional()
      .messages({
        ...messages,
        'string.base': 'Tên chủ tài khoản phải là chuỗi',
      }),
    bankName: Joi.string()
      .allow(null)
      .optional()
      .messages({
        ...messages,
        'string.base': 'Tên ngân hàng phải là chuỗi',
      }),
  })
    .allow(null) // Cho phép bankAccountData là null
    .optional() // Không bắt buộc phải có bankAccountData
    .messages({
      ...messages,
      'object.base': 'Dữ liệu tài khoản ngân hàng phải là một đối tượng',
    }),
}).options({ stripUnknown: true });

// Schema validation cho transactionId trong params
const transactionIdSchema = Joi.object({
  transactionId: Joi.number()
    .integer()
    .min(1)
    .required()
    .messages({
      ...messages,
      'number.base': 'ID giao dịch phải là số',
      'number.integer': 'ID giao dịch phải là số nguyên',
      'number.min': 'ID giao dịch không hợp lệ',
      'any.required': 'ID giao dịch là bắt buộc',
    }),
}).options({ stripUnknown: true });

// Schema validation cho lấy danh sách giao dịch
const getTransactionsSchema = Joi.object({
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
  limit: Joi.number()
    .integer()
    .min(1)
    .max(100)
    .default(20)
    .messages({
      ...messages,
      'number.base': 'Giới hạn phải là số',
      'number.integer': 'Giới hạn phải là số nguyên',
      'number.min': 'Giới hạn phải lớn hơn hoặc bằng 1',
      'number.max': 'Giới hạn không được vượt quá 100',
    }),
  lastId: Joi.number()
    .integer()
    .min(1)
    .allow(null)
    .messages({
      ...messages,
      'number.base': 'ID cuối cùng phải là số',
      'number.integer': 'ID cuối cùng phải là số nguyên',
      'number.min': 'ID cuối cùng không hợp lệ',
    }),
}).options({ stripUnknown: true });

export default {
  transaction: transactionSchema,
  transactionId: transactionIdSchema,
  updateTransaction: transactionUpdateSchema,
  getTransactions: getTransactionsSchema,
  async validateUser(userId) {
    try {
      await validateUser(userId);
    } catch (error) {
      throw error;
    }
  },
};
