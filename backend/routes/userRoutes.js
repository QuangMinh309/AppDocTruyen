import express from "express";
import validate from "../middlewares/validate.js";
const router = express.Router();
import UserValidation from "../validators/userValidation.js";
import UserController from "../controllers/userController.js";

router.post('/', UserController.createUser);
router.get('/:userId', validate(UserValidation.idSchema), UserController.getUserById);
router.get('/email', UserController.getUserByEmail);
router.get('/', validate(UserValidation.idSchema), UserController.getAllUsers);
router.put('/:userId', validate(UserValidation.update), UserController.updateUser);
router.delete('/:userId', validate(UserValidation.idSchema), UserController.deleteUser);

export default router;