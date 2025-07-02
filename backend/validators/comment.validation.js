import Joi from 'joi';
import ApiError from '../utils/api_error.util.js';


const createCommentSchema = Joi.object({
    action: Joi.string().valid('CREATE_COMMENT').required().messages({
        'any.required': 'action là bắt buộc',
        'any.only': 'action chỉ được phép là CREATE_COMMENT',
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

const updateCommentSchema = Joi.object({
    action: Joi.string().valid('UPDATE_COMMENT').required().messages({
        'any.required': 'action là bắt buộc',
        'any.only': 'action chỉ được phép là UPDATE_COMMENT',
    }),
    payload: Joi.object({
        commentId: Joi.number().integer().required().messages({
            'number.base': 'commentId phải là số',
            'number.integer': 'commentId phải là số nguyên',
            'any.required': 'commentId là bắt buộc',
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

const deleteCommentSchema = Joi.object({
    action: Joi.string().valid('DELETE_COMMENT').required().messages({
        'any.required': 'action là bắt buộc',
        'any.only': 'action chỉ được phép là DELETE_COMMENT',
    }),
    payload: Joi.object({
        commentId: Joi.number().integer().required().messages({
            'number.base': 'commentId phải là số',
            'number.integer': 'commentId phải là số nguyên',
            'any.required': 'commentId là bắt buộc',
        }),
    }).required().messages({
        'any.required': 'payload là bắt buộc',
    }),
});

const fetchCommentByStorySchema = Joi.object({
    action: Joi.string().valid('FETCH_COMMENT_BY_STORY').required().messages({
        'any.required': 'action là bắt buộc',
        'any.only': 'action chỉ được phép là FETCH_COMMENT_BY_STORY',
    }),
    payload: Joi.object({
    }).required().messages({
        'any.required': 'payload là bắt buộc',
    }),
});
const fetchCommentByChapterSchema = Joi.object({
    action: Joi.string().valid('FETCH_COMMENT_BY_CHAPTER').required().messages({
        'any.required': 'action là bắt buộc',
        'any.only': 'action chỉ được phép là FETCH_COMMENT_BY_CHAPTER',
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
    createCommentSchema,
    updateCommentSchema,
    deleteCommentSchema,
    fetchCommentByStorySchema,
    fetchCommentByChapterSchema
};