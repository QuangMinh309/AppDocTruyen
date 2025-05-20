import UserService from '../services/userService.js';
import userSchema from'../validators/userValidation.js';

const UserController = {
  async createUser(req, res, next) {
    try {
      const { error } = userSchema.create.validate(req.body);
      if (error) throw Object.assign(new Error(error.details[0].message), { statusCode: 400 });

      const user = await UserService.createUser(req.body);
      res.status(201).json(user);
    } catch (error) {
      next(error);
    }
  },

  async getUserById(req, res, next) {
    try {
      const { error } = userSchema.getById.validate(req.params);
      if (error) throw Object.assign(new Error(error.details[0].message), { statusCode: 400 });

      const user = await UserService.getUserById(req.params.userId);
      res.status(200).json(user);
    } catch (error) {
      next(error);
    }
  },

  async getUserByEmail(req, res, next) {
    try {
      const { error } = userSchema.getByEmail.validate(req.query);
      if (error) throw Object.assign(new Error(error.details[0].message), { statusCode: 400 });

      const user = await UserService.getUserByEmail(req.query.mail);
      res.status(200).json(user);
    } catch (error) {
      next(error);
    }
  },

  async getAllUsers(req, res, next) {
    try {
      const users = await UserService.getAllUsers(req.query.limit);
      res.status(200).json(users);
    } catch (error) {
      next(error);
    }
  },

  async updateUser(req, res, next) {
    try {
      const { error } = userSchema.update.validate(req.body);
      if (error) throw Object.assign(new Error(error.details[0].message), { statusCode: 400 });

      const user = await UserService.updateUser(req.params.userId, req.body);
      res.status(200).json(user);
    } catch (error) {
      next(error);
    }
  },

  async deleteUser(req, res, next) {
    try {
      const { error } = userSchema.delete.validate(req.params);
      if (error) throw Object.assign(new Error(error.details[0].message), { statusCode: 400 });

      const result = await UserService.deleteUser(req.params.userId);
      res.status(200).json(result);
    } catch (error) {
      next(error);
    }
  },
};

export default UserController;