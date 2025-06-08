import Joi from 'joi';

// Validation cho upload ảnh
const uploadImageValidation = (req, res, next) => {
    console.log('req.file in validation:', req.file); // Debug
    if (!req.file) {
        return res.status(400).json({ message: 'File ảnh là bắt buộc' });
    }
    next();
};

// // Validation cho getImageUrl
// const getImageUrlValidationSchema = Joi.object({
//     imageId: Joi.string().required().messages({
//         'any.required': 'imageId là bắt buộc',
//         'string.base': 'Image ID phải là chuỗi',
//         'string.empty': 'imageId không được để trống',
//     }),
// });

const getImageUrlValidation = (req, res, next) => {
    const { error } = getImageUrlValidationSchema.validate(req.params);
    if (error) {
        return res.status(400).json({ message: error.details[0].message });
    }
    next();
};


export {
    uploadImageValidation,
    getImageUrlValidation,
};
