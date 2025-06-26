import Joi from 'joi';
import ApiError from '../utils/api_error.util.js';


const createChatSchema = Joi.object({
    action: Joi.string().valid('CREATE_CHAT').required().messages({
        'any.required': 'action là bắt buộc',
        'any.only': 'action chỉ được phép là CREATE_CHAT',
    }),
    payload: Joi.object({
        content: Joi.string().allow(null).optional().messages({
            'string.base': 'content phải là chuỗi',
        }),
        commentPicData: Joi.string().allow(null).optional().messages({
            'string.base': 'commentPicData phải là chuỗi Base64',
        }),
    }).required().messages({
        'any.required': 'payload là bắt buộc',
    }),
});

const updateChatSchema = Joi.object({
    action: Joi.string().valid('UPDATE_CHAT').required().messages({
        'any.required': 'action là bắt buộc',
        'any.only': 'action chỉ được phép là UPDATE_CHAT',
    }),
    payload: Joi.object({
        chatId: Joi.number().integer().required().messages({
            'number.base': 'chatId phải là số',
            'number.integer': 'chatId phải là số nguyên',
            'any.required': 'chatId là bắt buộc',
        }),
        content: Joi.string().allow(null).optional().messages({
            'string.base': 'content phải là chuỗi',
        }),
        commentPicData: Joi.string().allow(null).optional().messages({
            'string.base': 'commentPicData phải là chuỗi Base64',
        }),
    }).required().messages({
        'any.required': 'payload là bắt buộc',
    }),
});

const deleteChatSchema = Joi.object({
    action: Joi.string().valid('DELETE_CHAT').required().messages({
        'any.required': 'action là bắt buộc',
        'any.only': 'action chỉ được phép là DELETE_CHAT',
    }),
    payload: Joi.object({
        chatId: Joi.number().integer().required().messages({
            'number.base': 'chatId phải là số',
            'number.integer': 'chatId phải là số nguyên',
            'any.required': 'chatId là bắt buộc',
        }),
    }).required().messages({
        'any.required': 'payload là bắt buộc',
    }),
});

const fetchChatByCommunitySchema = Joi.object({
    action: Joi.string().valid('FETCH_CHAT_BY_COMMUNITY').required().messages({
        'any.required': 'action là bắt buộc',
        'any.only': 'action chỉ được phép là FETCH_CHAT_BY_COMMUNITY',
    }),
    payload: Joi.object({
    }).required().messages({
        'any.required': 'payload là bắt buộc',
    }),
});

export const validateMessage = (schema, data) => {
    const { error, value } = schema.validate(data, { abortEarly: false });
    if (error) {
        const errorMessages = error.details.map((detail) => detail.message).join(', ');
        throw new ApiError(errorMessages, 400);
    }
    return value;
};

export {
    createChatSchema,
    updateChatSchema,
    deleteChatSchema,
    fetchChatByCommunitySchema,
};