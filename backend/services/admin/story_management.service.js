import { sequelize } from '../../models/index.js';
import { handleTransaction } from '../../utils/handle_transaction.util.js';
import ApiError from '../../utils/api_error.util.js';
import NotificationService from '../notification.service.js';

const Story = sequelize.models.Story;

const StoryManagerService = {
  async approveStory(storyId, approvalData) {
    return await handleTransaction(async (transaction) => {
      const story = await Story.findByPk(storyId, { transaction });
      if (!story) {
        throw new ApiError('Truyện không tồn tại', 404);
      }

      if (story.status !== 'pending') {
        throw new ApiError('Truyện không ở trạng thái chờ duyệt', 400);
      }

      const finalStatus =
        approvalData.status === 'approved' ? 'update' : 'rejected';

      await story.update(
        {
          ageRange: approvalData.ageRange,
          status: finalStatus,
          createdAt: new Date(), 
        },
        { transaction }
      );

      await NotificationService.createNotification(
        'STORY_APPROVAL',
        `Your story "${story.storyName}" has been ${
          approvalData.status === 'approved'
            ? 'được chấp nhận và sẵn sàng update'
            : 'bị từ chối'
        }.`,
        story.storyId,
        story.userId,
        transaction
      );

      const formattedStory = {
        ...story.toJSON(),
        createdAt: story.createdAt.toISOString().split('T')[0],
        updatedAt: story.updatedAt.toISOString().split('T')[0],
      };

      return {
        story: formattedStory,
        result: approvalData.status,
      };
    });
  },
};

export default StoryManagerService;
