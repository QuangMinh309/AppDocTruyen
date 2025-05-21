import express from "express";
import roleController from "../controllers/roleController.js";
import validate from "../middlewares/validate.js";
import {
  createRoleSchema,
  updateRoleSchema,
} from "../validators/roleValidation.js";

const router = express.Router();

router.post("/", validate(createRoleSchema), roleController.createRole);
router.get("/", roleController.getAllRoles);
router.get("/:id", roleController.getRoleById);
router.put("/:id", validate(updateRoleSchema), roleController.updateRole);
router.delete("/:id", roleController.deleteRole);

export default router;
