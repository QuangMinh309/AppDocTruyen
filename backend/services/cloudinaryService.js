import cloudinary from '../config/cloudinary.js';  // Sửa lại để dùng import

// Hàm lấy URL của ảnh từ Cloudinary
const getImageUrlFromCloudinary = (imageId) => {
    return cloudinary.url(imageId, {
      fetch_format: 'auto', // tự động chọn định dạng
      quality: 'auto',      // tự động chọn chất lượng
    });
};


const uploadImageToCloudinary  = async (file, folderPath) => {
  const sanitize = (str) => str.replace(/[^\w\-/]/g, '_');
  const safeFolder = sanitize(folderPath);  
  const safeName = sanitize(file.originalname);

  try {
    const result = await cloudinary.uploader.upload(file.path, {
      folder: safeFolder,
      public_id: safeName,
      overwrite: true,
      resource_type: "auto"
    });

    return {
      public_id: result.public_id,
      url: result.secure_url
    };
  } catch (error) {
    throw new Error(`Upload failed: ${error.message}`);
  }
};
// Export theo chuẩn ES Module
export {
  getImageUrlFromCloudinary,
  uploadImageToCloudinary
};