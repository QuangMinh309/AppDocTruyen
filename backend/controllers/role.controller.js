import RoleService from '../services/role.service.js'

const RoleController = {
  async createRole(req, res, next) {
    try {
      const role = await RoleService.createRole(req.body)
      res.status(201).json(role)
    } catch (err) {
      next(err)
    }
  },

  async getRoleById(req, res, next) {
    try {
      const role = await RoleService.getRoleById(req.params.id)
      res.json(role)
    } catch (err) {
      next(err)
    }
  },

  async updateRole(req, res, next) {
    try {
      const updatedRole = await RoleService.updateRole(req.params.id, req.body)
      res.json(updatedRole)
    } catch (err) {
      next(err)
    }
  },

  async deleteRole(req, res, next) {
    try {
      await RoleService.deleteRole(req.params.id)
      res.status(204).send()
    } catch (err) {
      next(err)
    }
  },
}

export default RoleController
