import jwt from "jsonwebtoken";
import { models } from "../models/index.js";
import ApiError from "../utils/apiError.js";

export const authenticateJWT = async (req, res, next) => {
  try {
    const authHeader = req.headers.authorization;

    if (!authHeader || !authHeader.startsWith("Bearer ")) {
      return next(new ApiError("Không có token xác thực", 401));
    }

    const token = authHeader.split(" ")[1];
    const decoded = jwt.verify(token, process.env.JWT_SECRET);

    const user = await models.User.findByPk(decoded.userId, {
      attributes: { exclude: ["password"] },
    });

    if (!user) {
      return next(new ApiError("Người dùng không tồn tại", 404));
    }

    req.user = user;
    next();
  } catch (error) {
    if (error instanceof jwt.JsonWebTokenError) {
      return next(new ApiError("Token không hợp lệ", 401));
    }
    if (error instanceof jwt.TokenExpiredError) {
      return next(new ApiError("Token đã hết hạn", 401));
    }
    return next(new ApiError("Lỗi xác thực", 500));
  }
};