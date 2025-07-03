import { sequelize } from '../../models/index.js'
import ApiError from '../../utils/api_error.util.js'
import { validateFollowOperation } from '../../utils/user.util.js'

const Follow = sequelize.models.Follow

const unfollowUser = async (followerId, followedId) => {
  try {
    const { follower, followed } = await validateFollowOperation(
      followerId,
      followedId
    )

    const existingFollow = await Follow.findOne({
      where: { followId: followerId, followedId },
    })

    if (!existingFollow) {
      throw new ApiError('Bạn chưa theo dõi người dùng này', 400)
    }

    await existingFollow.destroy()
    await followed.decrement('followerNum')

    return { message: 'Bỏ theo dõi người dùng thành công' }
  } catch (err) {
   
    if (err instanceof ApiError) throw err
    throw new ApiError('Lỗi khi bỏ theo dõi người dùng', 500)
  }
}

export default unfollowUser
