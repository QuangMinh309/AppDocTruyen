import ApiError from '../../utils/api_error.util.js'

const getCurrentUser = async (userId) => {
  try {
    const user = await this.getUserById(userId)
    return user
  } catch (err) {
    if (err instanceof ApiError) throw err
    throw new ApiError('Lỗi khi lấy thông tin người dùng hiện tại', 500)
  }
}

export default getCurrentUser
