import jwt from "jsonwebtoken";
import { sequelize } from "../models/index.js";
const User = sequelize.models.User;
const Role = sequelize.models.Role;
import ApiError from "../utils/apiError.js";

export const authenticate = async (req, res, next) => {
  try {
    const authHeader = req.headers.authorization;

    if (!authHeader || !authHeader.startsWith("Bearer ")) {
      return next(new ApiError("Không có token xác thực", 401));
    }

    const token = authHeader.split(" ")[1];
    const decoded = jwt.verify(token, process.env.JWT_SECRET);

    const user = await User.findByPk(decoded.userId, {
      attributes: { exclude: ["password"] },
      include: [
        { model: Role, as: "role", attributes: ["roleId", "roleName"] },
      ],
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

export const authorizeRoles = (...roles) => {
  return (req, res, next) => {
    if (!req.user || !req.user.role) {
      return next(new ApiError("Không có quyền truy cập", 403));
    }

    if (!roles.includes(req.user.role.roleName)) {
      return next(
        new ApiError("Bạn không có quyền thực hiện hành động này", 403)
      );
    }

    next();
  };
};

export const isResourceOwner = (req, res, next) => {
  const userId = parseInt(req.params.userId);

  if (req.user.userId !== userId && req.user.role.roleName !== "admin") {
    return next(
      new ApiError("Bạn không có quyền truy cập tài nguyên này", 403)
    );
  }

  next();
};
