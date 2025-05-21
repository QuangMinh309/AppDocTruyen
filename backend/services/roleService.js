import { sequelize } from "../models/index.js";
import ApiError from "../utils/apiError.js";

const Role = sequelize.models.Role;

const createRole = async (data) => {
  return await Role.create(data);
};

const getRoleById = async (id) => {
  const role = await Role.findByPk(id);
  if (!role) throw new ApiError("Role not found", 404);
  return role;
};

const updateRole = async (id, data) => {
  const role = await Role.findByPk(id);
  if (!role) throw new ApiError("Role not found", 404);
  return await role.update(data);
};

const deleteRole = async (id) => {
  const role = await Role.findByPk(id);
  if (!role) throw new ApiError("Role not found", 404);
  return await role.destroy();
};

const getAllRoles = async () => {
  return await Role.findAll();
};

export default {
  createRole,
  getRoleById,
  updateRole,
  deleteRole,
  getAllRoles,
};
