import cloudinary from '../config/cloudinary.js' // Sửa lại để dùng import

// Hàm lấy URL của ảnh từ Cloudinary
const getImageUrlFromCloudinary = (imageId) => {
  return cloudinary.url(imageId, {
    fetch_format: 'auto', // tự động chọn định dạng
    quality: 'auto', // tự động chọn chất lượng
  })
}

const uploadImageToCloudinary = async (imageId) => {
  try {
    const url = await new Promise((resolve, reject) => {
      cloudinary.url(imageId, (error, url) => {
        if (error || !url) reject(new Error('Không tìm thấy ảnh'))
        else resolve(url)
      })
    })
    return url
  } catch (error) {
    throw new ApiError(404, 'Không tìm thấy ảnh', [error.message])
  }
}
// Export theo chuẩn ES Module
export { getImageUrlFromCloudinary, uploadImageToCloudinary }
