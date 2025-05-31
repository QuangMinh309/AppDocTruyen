import { sequelize } from '../../models/index.js'
import ApiError from '../../utils/api_error.util.js'

const User = sequelize.models.User
const Role = sequelize.models.Role

const AdminService = {
  async lockUser(userId) {
    try {
      const user = await User.findByPk(userId)
      if (!user) throw new ApiError('Người dùng không tồn tại', 404)

      await user.update({ status: 'locked' })
      return { message: 'Khóa người dùng thành công' }
    } catch (err) {
      if (err instanceof ApiError) throw err
      throw new ApiError('Lỗi khi khóa người dùng', 500)
    }
  },

  async unlockUser(userId) {
    try {
      const user = await User.findByPk(userId)
      if (!user) throw new ApiError('Người dùng không tồn tại', 404)

      await user.update({ status: 'active' })
      return { message: 'Mở khóa người dùng thành công' }
    } catch (err) {
      if (err instanceof ApiError) throw err
      throw new ApiError('Lỗi khi mở khóa người dùng', 500)
    }
  },

  async getAllUsers(offset = 0, limit = 15) {
    try {
      const users = await User.findAll({
        offset,
        limit,
        order: [['createdAt', 'DESC']],
        attributes: { exclude: ['password'] },
        include: [
          {
            model: Role,
            as: 'role',
            attributes: ['roleId', 'roleName'],
          },
        ],
      })

      return users.map((user) => user.toJSON())
    } catch (err) {
      throw new ApiError('Lỗi khi lấy người dùng', 500)
    }
  },
}

export default AdminService
