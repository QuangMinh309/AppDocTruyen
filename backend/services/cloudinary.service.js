import cloudinary from '../config/cloudinary.js' // Sửa lại để dùng import
import ApiError from '../utils/api_error.util.js';
import streamifier from 'streamifier';

// Hàm lấy URL của ảnh từ Cloudinary
const getImageUrlFromCloudinary = async (imageId) => {
  try {
    // Kiểm tra tồn tại của publicId
    if (!imageId) return "no picture."
    const result = await cloudinary.api.resource(imageId, { resource_type: 'image' });
    return cloudinary.url(imageId, {
      fetch_format: 'auto',
      quality: 'auto',
    });
  } catch (error) {
    console.log(error )
    console.log(`không thể lấy ảnh từ cloudinary!`, 500)
    return ""
  }
};


const uploadImageToCloudinary = async (fileBuffer, des = "") => {
  try {
    console.log('Received fileBuffer:', fileBuffer); // Debug log
    const streamUpload = () => {
      return new Promise((resolve, reject) => {
        const stream = cloudinary.uploader.upload_stream(
          { folder: des },    // /Home is default folder in Cloudinary 
          (error, result) => {
            if (error) reject(error);
            else resolve(result);
          });
        streamifier.createReadStream(fileBuffer).pipe(stream);
      });
    };

    const result = await streamUpload();
    return result;
  } catch (error) {
    throw new ApiError('Lỗi upload lên Cloudinary', 500);
  }
};

const uploadBase64ToCloudinary = async (base64Image, des = "") => {
  try {
    let base64Data = base64Image;

    // Nếu có prefix "data:image/...;base64," → loại bỏ để lấy phần base64 thuần
    const matches = base64Image.match(/^data:(.+);base64,(.+)$/);
    if (matches) {
      base64Data = matches[2]; // lấy phần sau "base64,"
    }

    // Kiểm tra nếu vẫn không có dữ liệu
    if (!base64Data || typeof base64Data !== 'string') {
      throw new ApiError('Base64 không hợp lệ hoặc bị rút gọn', 400);
    }

    // Tạo buffer từ base64
    const buffer = Buffer.from(base64Data, 'base64');
    return await uploadImageToCloudinary(buffer, des);

  } catch (error) {
    console.error('Lỗi upload base64:', error.message);
    if (error instanceof ApiError) throw error;
    throw new ApiError('Lỗi upload base64 lên Cloudinary', 500);
  }
};


const deleteImageOnCloudinary = async (publicId) => {
  try {
    const result = await cloudinary.uploader.destroy(publicId);
    console.log('Deleted image result:', result);
    return result;
  } catch (error) {
    console.error('Error deleting image from Cloudinary:', error);
    throw error;
  }
}

// Export theo chuẩn ES Module
export { getImageUrlFromCloudinary, uploadImageToCloudinary, uploadBase64ToCloudinary, deleteImageOnCloudinary };
