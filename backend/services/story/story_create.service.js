import { sequelize } from '../../models/index.js';
import { handleTransaction } from '../../utils/handle_transaction.util.js';
import { uploadImageToCloudinary } from '../cloudinary.service.js';
import { formatDate } from '../../utils/date.util.js';
import ApiError from '../../utils/api_error.util.js';
import NotificationService from '../notification.service.js';
import { notifyUser } from '../../utils/notify_user.util.js';

const Story = sequelize.models.Story;
const StoryCategory = sequelize.models.StoryCategory;
const User = sequelize.models.User;
const Role = sequelize.models.Role;

const createStory = async (storyData, userId, file) => {
  return await handleTransaction(async (transaction) => {
    // console.log('createStory - Received storyData:', storyData);
    // console.log('createStory - Received file:', file);

    let coverImgId = null;

    if (file) {
      try {
        const uploadResult = await uploadImageToCloudinary(
          file.buffer,
          'stories'
        );
        // console.log('createStory - Cloudinary upload result:', uploadResult);
        coverImgId = uploadResult.public_id;
      } catch (error) {
        // console.error('createStory - Cloudinary upload error:', error);
        throw new ApiError('Tải ảnh lên thất bại', 500);
      }
    } else {
      console.warn('createStory - No file provided for cover image');
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
          categoryId: Number(categoryId),
        })
      );
      await StoryCategory.bulkCreate(categoryAssociations, { transaction });
    }

    const author = await User.findByPk(userId, {
      attributes: ['userName'],
      transaction,
    });

    const adminRole = await Role.findOne({
      where: { roleName: 'admin' },
      transaction,
    });

    if (adminRole) {
      const admins = await User.findAll({
        where: { roleId: adminRole.roleId },
        transaction,
      });

      // Send notification to admin
      if (admins.length > 0) {
        const adminNotifications = admins.map((admin) =>
          NotificationService.createNotification(
            'STORY_PENDING_APPROVAL',
            `Truyện mới ${story.storyName} của tác giả ${author?.userName || 'Unknown'
            } cần được duyệt (vui lòng viết truyện phù hợp độ tuổi).`,
            story.storyId,
            admin.userId,
            transaction
          )
        );

        await Promise.all(adminNotifications);

        const notificationIds = adminNotifications.map(noti => noti.userId);
        notifyUser(notificationIds);

      }
    }

    const formattedStory = {
      ...story.toJSON(),
      createdAt: formatDate(story.createdAt),
      updatedAt: formatDate(story.updatedAt),
    };

    return formattedStory;
  });
};

export default createStory;
