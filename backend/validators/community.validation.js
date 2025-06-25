import Joi from 'joi';

export const validateSearchMember = Joi.object({
    searchTerm: Joi.string()
        .trim()
        .required()
        .messages({ 'string.empty': 'Từ khóa tìm kiếm là bắt buộc' }),
});
