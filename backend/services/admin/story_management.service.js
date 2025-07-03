import { sequelize } from '../../models/index.js';
import { handleTransaction } from '../../utils/handle_transaction.util.js';
import { formatDate } from '../../utils/date.util.js';
import ApiError from '../../utils/api_error.util.js';
import NotificationService from '../notification.service.js';
import { notifyUser } from '../../utils/notify_user.util.js';

const { Story } = sequelize.models;

const StoryManagerService = {
  async approveStory(storyId, approvalData) {
    return await handleTransaction(async (transaction) => {
      const story = await Story.findByPk(storyId, { transaction });
      if (!story) throw new ApiError('Truyện không tồn tại', 404);
      if (story.status !== 'pending')
        throw new ApiError('Truyện không ở trạng thái chờ duyệt', 400);

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
        `Truyện ${story.storyName} của bạn ${
          approvalData.status === 'approved'
            ? `được chấp nhận với độ tuổi ${story.ageRange}+`
            : 'bị từ chối'
        }.`,
        story.storyId,
        story.userId,
        transaction
      );


      if (approvalData.status === 'approved') {
        const message = `Tác giả bạn theo dõi vừa đăng truyện mới: ${story.storyName}`;
        await NotificationService.notifyFollowers(
          story.userId,
          story.storyId,
          message,
          transaction
        );
      }
       notifyUser(story.userId)

      const formattedStory = {
        ...story.toJSON(),
        createdAt: formatDate(story.createdAt),
        updatedAt: formatDate(story.updatedAt),
      };

      return {
        story: formattedStory,
        result: approvalData.status,
      };
    });
  },
};

export default StoryManagerService;
