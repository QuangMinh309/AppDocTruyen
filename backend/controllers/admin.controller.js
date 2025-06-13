import UserManagerService from '../services/admin/user_management.service.js';
import StoryManagerService from '../services/admin/story_management.service.js';

const AdminController = {
  // Quản lý user
  async getAllUsers(req, res, next) {
    try {
      const offset = parseInt(req.query.offset) || 0;
      const limit = 15;

      const result = await UserManagerService.getAllUsers(offset, limit);

      res.status(200).json({
        success: true,
        data: result,
      });
    } catch (error) {
      next(error);
    }
  },

  async lockUser(req, res, next) {
    try {
      const { userId } = req.params;

      const result = await UserManagerService.lockUser(parseInt(userId));

      res.status(200).json({
        success: true,
        message: result.message,
      });
    } catch (error) {
      next(error);
    }
  },

  async unlockUser(req, res, next) {
    try {
      const { userId } = req.params;

      const result = await UserManagerService.unlockUser(parseInt(userId));

      res.status(200).json({
        success: true,
        message: result.message,
      });
    } catch (error) {
      next(error);
    }
  },

  // Quản lý story
  async approveStory(req, res, next) {
    try {
      const storyId = req.params.storyId;
      const approvalData = req.body;

      const { story, result } = await StoryManagerService.approveStory(
        storyId,
        approvalData,
      );

      const message =
        result === 'approved'
          ? 'Truyện đã được duyệt. Bạn có thể cập nhật và hoàn thành truyện.'
          : 'Truyện bị từ chối. Vui lòng kiểm tra và cập nhật lại.';

      res.status(200).json({
        success: true,
        data: story,
        message,
      });
    } catch (error) {
      next(error);
    }
  },
};

export default AdminController;
