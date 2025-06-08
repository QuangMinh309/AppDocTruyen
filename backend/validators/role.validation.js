import Joi from 'joi'

export const createRoleSchema = Joi.object({
  roleName: Joi.string().min(2).max(50).required(),
  description: Joi.string().max(255).optional().allow(''),
})

export const updateRoleSchema = Joi.object({
  roleName: Joi.string().min(2).max(50).optional(),
  description: Joi.string().max(255).optional().allow(''),
})
