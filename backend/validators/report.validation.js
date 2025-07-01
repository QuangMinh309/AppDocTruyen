import Joi from 'joi';

export const getDailyRevenueQuery = Joi.object({
  year: Joi.number().integer().min(2000).required().label('Năm'),
  month: Joi.number().integer().min(1).max(12).required().label('Tháng'),
});
