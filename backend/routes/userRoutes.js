import express from "express";
import UserController from "../controllers/userController.js";
import {
  authenticate,
  authorizeRoles,
  isResourceOwner,
} from "../middlewares/authMiddleware.js";
import validate from "../middlewares/validate.js";
import userValidation from "../validators/userValidation.js";

const router = express.Router();

router.post(
  "/auth/register",
  validate(userValidation.register),
  UserController.register
);
router.post(
  "/auth/login",
  validate(userValidation.login),
  UserController.login
);

// Các route cần đăng nhập
router.use(authenticate);

// User profile routes
router.get("/users/me", UserController.getCurrentUser);
router.post("/auth/refresh-token", UserController.refreshToken);

// User management routes (with owner/admin check)
router.get(
  "/users/:userId",
  validate(userValidation.userId, "params"),
  UserController.getUserById
);
router.put(
  "/users/:userId",
  validate(userValidation.userId, "params"),
  validate(userValidation.updateUser),
  isResourceOwner,
  UserController.updateUser
);
router.post(
  "/users/:userId/change-password",
  validate(userValidation.userId, "params"),
  validate(userValidation.changePassword),
  isResourceOwner,
  UserController.changePassword
);

// Admin-only routes
router.get(
  "/users",
  validate(userValidation.pagination, "query"),
  authorizeRoles("admin"),
  UserController.getAllUsers
);
router.delete(
  "/users/:userId",
  validate(userValidation.userId, "params"),
  authorizeRoles("admin"),
  UserController.deleteUser
);
router.patch(
  "/users/:userId/toggle-premium",
  validate(userValidation.userId, "params"),
  authorizeRoles("admin"),
  UserController.togglePremium
);

export default router;
