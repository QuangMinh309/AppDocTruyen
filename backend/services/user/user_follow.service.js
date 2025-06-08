import { sequelize } from '../../models/index.js'
import ApiError from '../../utils/api_error.util.js'
import { validateFollowOperation } from '../../utils/user.util.js'

const Follow = sequelize.models.Follow

const followUser = async (followerId, followedId) => {
  try {
    if (followerId === followedId) {
      throw new ApiError('Không thể tự theo dõi chính mình', 400)
    }

    const { follower, followed } = await validateFollowOperation(
      followerId,
      followedId
    )

    const existingFollow = await Follow.findOne({
      where: { followId: followerId, followedId },
    })

    if (existingFollow) {
      throw new ApiError('Bạn đã theo dõi người dùng này', 400)
    }

    await Follow.create({
      followId: followerId,
      followedId,
    })

    await followed.increment('followerNum')

    return { message: 'Theo dõi người dùng thành công' }
  } catch (err) {
    if (err instanceof ApiError) throw err
    throw new ApiError('Lỗi khi theo dõi người dùng', 500)
  }
}

export default followUser
