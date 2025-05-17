import ApiError from "../utils/apiError.js";

const validate = (schema) => (req, res, next) => {
  const data = { ...req.body, ...req.params };
  const { error } = schema.validate(data, { abortEarly: false });
  if (error) {
    const message = error.details.map((detail) => detail.message).join(", ");
    throw new ApiError(message, 400);
  }
  next();
};

export default validate;
