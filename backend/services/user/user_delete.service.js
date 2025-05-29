import { sequelize } from '../../models/index.js'
import ApiError from '../../utils/api_error.util.js'
import { validateUser } from '../../utils/user.util.js'

const deleteUser = async (userId) => {
  try {
    const user = validateUser(userId)

    await sequelize.models.Follow.destroy({ where: { followId: userId } })
    await sequelize.models.Follow.destroy({ where: { followedId: userId } })
    await sequelize.models.ReadList.destroy({ where: { userId } })
    await sequelize.models.Story.destroy({ where: { userId } })
    await sequelize.models.Notification.destroy({ where: { userId } })
    await sequelize.models.PasswordReset.destroy({ where: { userId } })
    await sequelize.models.Purchase.destroy({ where: { userId } })

    await user.destroy()
    return { message: 'Xóa người dùng thành công' }
  } catch (err) {
    if (err instanceof ApiError) throw err
    throw new ApiError('Lỗi khi xóa người dùng', 500)
  }
}

export default deleteUser
