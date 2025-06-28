import { sequelize } from '../../models/index.js';
import ApiError from '../../utils/api_error.util.js';

const Follow = sequelize.models.Follow;

const checkFollowStatus = async (followerId, followedId) => {
  try {
    if (followerId === followedId) {
      throw new ApiError(
        'Không thể kiểm tra trạng thái theo dõi chính mình',
        400
      );
    }

    const followRecord = await Follow.findOne({
      where: { followId: followerId, followedId },
    });

    return { isFollowing: !!followRecord };
  } catch (err) {
    if (err instanceof ApiError) throw err;
    throw new ApiError('Lỗi khi kiểm tra trạng thái theo dõi', 500);
  }
};

export default checkFollowStatus;
