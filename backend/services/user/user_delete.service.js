import { sequelize } from '../../models/index.js'
import ApiError from '../../utils/api_error.util.js'
import { validateUser } from '../../utils/user.util.js'
import { deleteImageOnCloudinary } from '../cloudinary.service.js'
import deleteStory from '../story/story_delete.service.js'

const Follow = sequelize.models.Follow
const NameList = sequelize.models.NameList
const Story = sequelize.models.Story
const Notification = sequelize.models.Notification
const PasswordReset = sequelize.models.PasswordReset
const Purchase = sequelize.models.Purchase

const deleteUser = async (userId) => {
  try {
    const user = await validateUser(userId)

    if (user.avatarId) {
      try {
        await deleteImageOnCloudinary(user.avatarId)
      } catch (error) {
        throw new ApiError('Xóa ảnh đại diện thất bại', 500)
      }
    }

    if (user.backgroundId) {
      try {
        await deleteImageOnCloudinary(user.backgroundId);
      } catch (error) {
        throw new ApiError('Xóa ảnh bìa thất bại', 500)
      }
    }

    await Follow.destroy({ where: { followId: userId } })
    await Follow.destroy({ where: { followedId: userId } })

    await NameList.destroy({ where: { userId } })

    const stories = await Story.findAll({ where: { userId } })
    for (const story of stories) {
      await deleteStory(story.storyId)
    }
    await Notification.destroy({ where: { userId } })
    await PasswordReset.destroy({ where: { userId } })
    await Purchase.destroy({ where: { userId } })

    await user.destroy()

    return { message: 'Xóa người dùng thành công' }
  } catch (err) {
    if (err instanceof ApiError) throw err
    throw new ApiError('Lỗi khi xóa người dùng', 500)
  }
}

export default deleteUser
