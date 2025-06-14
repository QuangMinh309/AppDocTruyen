import express from 'express'
import RoleController from '../controllers/role.controller.js'
import validate from '../middlewares/validate.middleware.js'
import {
  createRoleSchema,
  updateRoleSchema,
} from '../validators/role.validation.js'
import { authenticate } from '../middlewares/auth.middleware.js'

const router = express.Router()

router.use(authenticate)

router.post('/', validate(createRoleSchema), RoleController.createRole)
router.get('/:id', RoleController.getRoleById)
router.put('/:id', validate(updateRoleSchema), RoleController.updateRole)
router.delete('/:id', RoleController.deleteRole)

export default router
