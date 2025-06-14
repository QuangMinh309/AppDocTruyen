import { sequelize } from '../../models/index.js';
import { handleTransaction } from '../../utils/handle_transaction.util.js';
import { uploadImageToCloudinary } from '../cloudinary.service.js';
import { formatDate } from '../../utils/date.util.js';
import ApiError from '../../utils/api_error.util.js';

const Story = sequelize.models.Story;
const StoryCategory = sequelize.models.StoryCategory;

const createStory = async (storyData, userId, file) => {
  return await handleTransaction(async (transaction) => {
    let coverImgId = null;

    // Handle image upload if provided
    if (file) {
      try {
        const uploadResult = await uploadImageToCloudinary(
          file.buffer,
          'stories'
        );
        coverImgId = uploadResult.public_id;
      } catch (error) {
        throw new ApiError('Tải ảnh lên thất bại', 500);
      }
    }

    // Remove ageRange and ensure safeStoryData is an object
    const { ageRange, ...safeStoryData } = storyData || {};

    // Validate required fields
    if (!safeStoryData.storyName) {
      throw new ApiError('Tiêu đề truyện là bắt buộc', 400);
    }

    const story = await Story.create(
      {
        ...safeStoryData,
        userId,
        status: 'pending',
        viewNum: 0,
        voteNum: 0,
        chapterNum: 0,
        coverImgId,
      },
      { transaction }
    );

    // Handle categories if provided and valid
    if (
      Array.isArray(safeStoryData.categories) &&
      safeStoryData.categories.length > 0
    ) {
      const categoryAssociations = safeStoryData.categories.map(
        (categoryId) => ({
          storyId: story.storyId,
          categoryId: Number(categoryId), // Safe type conversion
        })
      );
      await StoryCategory.bulkCreate(categoryAssociations, { transaction });
    }

    // Format createdAt to YYYY-MM-DD
    const formattedStory = {
      ...story.toJSON(),
      createdAt: formatDate(story.createdAt),
      updatedAt: formatDate(story.updatedAt),
    };

    return formattedStory;
  });
};

export default createStory;
