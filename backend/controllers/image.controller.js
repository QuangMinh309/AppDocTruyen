import {
  getImageUrlFromCloudinary,
  uploadImageToCloudinary,
  deleteImageOnCloudinary
} from '../services/cloudinary.service.js'
import ApiError from '../utils/api_error.util.js'

export const getImageUrl = async (req, res) => {
  try {
    // console.log('Received request to get image URL for imageId:', req.params.imageId);
    const imageId = req.params.imageId
    const imageUrl = await getImageUrlFromCloudinary(imageId)
    res.json({ url: imageUrl })
  } catch (error) {
    if (error instanceof ApiError) {
      res.status(error.statusCode).json({ message: error.message, errors: error.errors });
    } else {
      res.status(500).json({ message: 'Lỗi server nội bộ', error: error.message });
    }

  }
}

export const uploadImage = async (req, res) => {
  try {
    if (!req.file) {
      throw new ApiError(400, 'Không có file được upload'); //  destination : "/user/abcd"
    }
    const result = await uploadImageToCloudinary(req.file.buffer, req.body.destination || '');
    res.json({ url: result.secure_url, publicId: result.public_id });
  } catch (error) {
    if (error instanceof ApiError) {
      res.status(error.statusCode).json({ message: error.message, errors: error.errors });
    } else {
      res.status(500).json({ message: 'Lỗi upload lên Cloudinary', error: error.message });
    }
  }
};

export const deleteImage = async (req, res) => {
  try {
    const imageId = req.params.imageId

    const result = await deleteImageOnCloudinary(imageId);
    if (!result) {
      throw new ApiError('Lỗi khi xóa hình ảnh', 400);
    }
    res.json({ message: 'Xóa hình ảnh thành công', publicId: imageId });
  } catch (error) {
    if (error instanceof ApiError) {
      res.status(error.statusCode).json({ message: error.message, errors: error.errors });
    } else {
      res.status(500).json({ message: 'Lỗi upload lên Cloudinary', error: error.message });
    }
  }
};
