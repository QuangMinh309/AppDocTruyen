import { sequelize } from '../models/index.js'
import ApiError from '../utils/api_error.util.js'

const Role = sequelize.models.Role

const RoleService = {
  async createRole(data) {
    return await Role.create(data)
  },

  async getRoleById(id) {
    const role = await Role.findByPk(id)
    if (!role) throw new ApiError('Role not found', 404)
    return role
  },

  async updateRole(id, data) {
    const role = await Role.findByPk(id)
    if (!role) throw new ApiError('Role not found', 404)
    return await role.update(data)
  },

  async deleteRole(id) {
    const role = await Role.findByPk(id)
    if (!role) throw new ApiError('Role not found', 404)
    return await role.destroy()
  },
}

export default RoleService
