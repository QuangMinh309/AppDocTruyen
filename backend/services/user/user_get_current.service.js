import ApiError from '../../utils/api_error.util.js'
import getUserById from './user_get_id.service.js'

const getCurrentUser = async (userId) => {
  try {
    const user = await getUserById(userId)
    return user
  } catch (err) {
    console.error("Lỗi gốc khi lấy user:", err);
    if (err instanceof ApiError) throw err
    throw new ApiError('Lỗi khi lấy thông tin người dùng hiện tại', 500)
  }
}

export default getCurrentUser
