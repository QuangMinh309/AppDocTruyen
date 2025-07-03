import ApiError from '../utils/api_error.util.js'

const validate = (schema, property = 'body') => {
  return (req, res, next) => {
    if (!schema || typeof schema.validate !== 'function') {
      return next(new ApiError('Lỗi hệ thống: Joi schema không hợp lệ', 500))
    }

    const { error } = schema.validate(req[property], { abortEarly: false })
    if (error) {
      // console.log(req[property])
      return next(
        new ApiError(
          `Dữ liệu không hợp lệ: ${error.details
            .map((d) => d.message)
            .join(', ')}`,
          400
        )
      )
    }
    next()
  }
}

export default validate
