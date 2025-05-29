import { sequelize } from '../../models/index.js'
import bcrypt from 'bcrypt'
import ApiError from '../../utils/api_error.util.js'
import { validateUser } from '../../utils/user.util.js'

const User = sequelize.models.User
const Role = sequelize.models.Role

const updateUser = async (userId, data) => {
  try {
    const user = await validateUser(userId)

    const { password, ...updateData } = data

    if (updateData.mail && updateData.mail !== user.mail) {
      const existingUser = await User.findOne({
        where: { mail: updateData.mail },
      })
      if (existingUser) {
        throw new ApiError('Email đã được sử dụng bởi người dùng khác', 400)
      }
    }

    if (password) {
      updateData.password = await bcrypt.hash(password, 10)
    }

    await user.update(updateData)

    const updatedUser = await User.findByPk(userId, {
      attributes: { exclude: ['password'] },
      include: [
        { model: Role, as: 'role', attributes: ['roleId', 'roleName'] },
      ],
    })

    return updatedUser.toJSON()
  } catch (err) {
    if (err instanceof ApiError) throw err
    throw new ApiError('Lỗi khi cập nhật người dùng', 500)
  }
}

export default updateUser
