import { sequelize } from '../../models/index.js';
import { validateStory } from '../../utils/story.util.js';
import { handleTransaction } from '../../utils/handle_transaction.util.js';
import {
  uploadImageToCloudinary,
  deleteImageOnCloudinary,
} from '../cloudinary.service.js';
import { formatDate } from '../../utils/date.util.js';
import ApiError from '../../utils/api_error.util.js';

const StoryCategory = sequelize.models.StoryCategory;

const updateStory = async (storyId, storyData, userId, file) => {
  return await handleTransaction(async (transaction) => {
    const story = await validateStory(storyId, userId);

    let coverImgId = story.coverImgId;

    if (file) {
      try {
        if (coverImgId) {
          try {
            await deleteImageOnCloudinary(coverImgId);
          } catch (error) {
            throw new ApiError('Xóa ảnh cũ thất bại', 500);
          }
        }

        const uploadResult = await uploadImageToCloudinary(
          file.buffer,
          'stories'
        );
        coverImgId = uploadResult.public_id;
      } catch (error) {
        throw new ApiError('Tải ảnh lên thất bại', 500);
      }
    }

    const { ageRange, categories, ...safeStoryData } = storyData || {};

    if (safeStoryData.storyName && !safeStoryData.storyName.trim()) {
      throw new ApiError('Tiêu đề truyện là bắt buộc', 400);
    }

    await story.update(
      {
        ...safeStoryData,
        coverImgId,
      },
      { transaction }
    );

    if (Array.isArray(categories) && categories.length > 0) {
      await StoryCategory.destroy({ where: { storyId }, transaction });

      // Create new category associations
      const categoryAssociations = categories.map((categoryId) => ({
        storyId,
        categoryId: Number(categoryId),
      }));
      await StoryCategory.bulkCreate(categoryAssociations, { transaction });
    }

    const formattedStory = {
      ...story.toJSON(),
      createdAt: formatDate(story.createdAt),
      updatedAt: formatDate(story.updatedAt),
    };

    return formattedStory;
  });
};

export default updateStory;
