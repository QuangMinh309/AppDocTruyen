import {
  getImageUrlFromCloudinary,
  uploadImageToCloudinary,
} from '../services/cloudinary.service.js'

export const getImageUrl = async (req, res) => {
  try {
    const imageId = req.params.imageId
    const imageUrl = getImageUrlFromCloudinary(imageId)
    res.json({ url: imageUrl })
  } catch (error) {
    console.error(error)
    res.status(500).send('Error retrieving image from Cloudinary')
  }
}

export const uploadImage = async (req, res) => {
  try {
    if (!req.file) {
      return res
        .status(400)
        .json({ success: false, error: 'No image file uploaded' })
    }

    const folder = req.body.folder || 'se114_novel_app_rsrc/user' // Có thể lấy từ form
    const imgInfo = await uploadImageToCloudinary(req.file, folder)

    res.json({
      url: imgInfo.url,
      id: imgInfo.public_id,
    })
  } catch (err) {
    res.status(500).json({ success: false, error: err.message })
  }
}
