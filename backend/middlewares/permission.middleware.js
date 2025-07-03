import { models } from '../models/index.js';
import ApiError from '../utils/api_error.util.js';

export const isStoryAuthor = async (req, res, next) => {
  try {
    const chapterId = req.params.chapterId;
    const storyId = req.params.storyId;
    const userId = req.user.userId;

    if (chapterId) {
      const chapter = await models.Chapter.findByPk(chapterId, {
        include: [
          {
            model: models.Story,
            as: 'story',
            attributes: ['storyId', 'userId'],
          },
        ],
      });

      if (!chapter) {
        return next(new ApiError('Chương không tồn tại', 404));
      }
      if (!chapter.story) {
        return next(
          new ApiError('Không tìm thấy truyện liên quan đến chương', 404)
        );
      }

      if (
        chapter.story.userId !== userId &&
        req.user.role.roleName !== 'admin'
      ) {
        return next(new ApiError('Bạn không có quyền với truyện này', 403));
      }

      req.storyId = chapter.story.storyId;
      return next();
    } else if (storyId) {
      const story = await models.Story.findByPk(storyId);
      if (!story) {
        return next(new ApiError('Truyện không tồn tại', 404));
      }

      if (story.userId !== userId && req.user.role.roleName !== 'admin') {
        return next(new ApiError('Bạn không có quyền với truyện này', 403));
      }

      req.storyId = storyId;
      return next();
    } else {
      return next(new ApiError('Thiếu thông tin truyện hoặc chương', 400));
    }
  } catch (error) {
    console.error('Lỗi khi kiểm tra quyền tác giả:', error);
    return next(new ApiError('Lỗi khi kiểm tra quyền tác giả', 500));
  }
};

export const canAccessChapter = async (req, res, next) => {
  try {
    if (!req.user || !req.user.userId) {
      return next(new ApiError('Bạn phải đăng nhập để đọc truyện', 401));
    }

    const chapterId = req.params.chapterId;
    const parsedChapterId = parseInt(chapterId);
    const userId = req.user.userId;

    const chapter = await models.Chapter.findByPk(parsedChapterId, {
      include: [{ model: models.Story, as: 'story' }],
    });

    if (!chapter) {
      return next(new ApiError('Chương không tồn tại', 404));
    }

    // Tác giả của truyện hoặc admin luôn được truy cập
    if (chapter.story.userId === userId || req.user.role.roleName === 'admin')
      return next();

    // Người dùng premium được truy cập tất cả các chương
    if (req.user.isPremium) return next();

    // Nếu chương không bị khóa, ai cũng đọc được
    if (!chapter.lockedStatus) return next();

    // Lấy tham số thời hạn truy cập
    const parameters = await models.Parameter.findOne();
    if (!parameters) {
      return next(new ApiError('Không tìm thấy tham số hệ thống', 500));
    }

    const now = new Date();

    // Kiểm tra nếu người dùng đã mua chương này
    const chapterPurchase = await models.Purchase.findOne({
      attributes: ['purchasedId', 'userId', 'chapterId', 'purchasedAt'],
      where: {
        userId,
        chapterId,
      },
    });
    if (chapterPurchase) {
      const purchaseDate = new Date(chapterPurchase.purchasedAt);
      const expiryDate = new Date(
        purchaseDate.getTime() +
          parameters.Chapter_Access_Duration * 24 * 60 * 60 * 1000
      );
      if (now <= expiryDate) return next();
      return next(new ApiError('Thời hạn truy cập chương đã hết', 403));
    }

    return next(new ApiError('Bạn cần mua chương này để truy cập', 403));
  } catch (error) {
    console.error('Lỗi khi kiểm tra quyền truy cập chương:', error);
    return next(new ApiError('Lỗi khi kiểm tra quyền truy cập chương', 500));
  }
};

export const canManageReadingList = async (req, res, next) => {
  try {
    const nameListId = req.params.nameListId || req.body.nameListId;
    const userId = req.user.userId;

    if (!nameListId) {
      return next(new ApiError('Thiếu nameListId', 400));
    }

    const nameList = await models.NameList.findByPk(nameListId);
    if (!nameList) {
      return next(new ApiError('Danh sách đọc không tồn tại', 404));
    }

    if (nameList.userId !== userId) {
      return next(
        new ApiError('Bạn không có quyền quản lý danh sách này', 403)
      );
    }

    next();
  } catch (error) {
    console.error('Lỗi khi kiểm tra quyền quản lý danh sách:', error);
    return next(new ApiError('Lỗi khi kiểm tra quyền quản lý danh sách', 500));
  }
};
