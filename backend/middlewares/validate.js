import ApiError from "../utils/apiError.js";

const validate = (schema, property = "body") => {
  return (req, res, next) => {
    const { error } = schema.validate(req[property], { abortEarly: false });
    if (error) {
      return next(
        new ApiError(
          `Dữ liệu không hợp lệ: ${error.details
            .map((d) => d.message)
            .join(", ")}`,
          400
        )
      );
    }
    next();
  };
};

export default validate;