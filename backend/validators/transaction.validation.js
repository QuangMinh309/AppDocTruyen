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
const transactionSchema = Joi.object({
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
  getTransactions: getTransactionsSchema,
  async validateUser(userId) {
    try {
      await validateUser(userId);
    } catch (error) {
      throw error;
    }
  },
};
