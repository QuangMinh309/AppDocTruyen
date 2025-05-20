import { models } from "../models/index.js";
import ApiError from "../utils/apiError.js";

// export const isAdmin = (req, res, next) => {
//   if (!req.user || req.user.role !== "ADMIN") {
//     return next(new ApiError("Không có quyền admin", 403));
//   }
//   next();
// };

export const isStoryAuthor = async (req, res, next) => {
  try {
    const storyId = req.params.storyId || req.body.storyId;
    const userId = req.user.userId;

    if (!storyId) {
      return next(new ApiError("Thiếu storyId", 400));
    }

    const story = await models.Story.findByPk(storyId);
    if (!story) {
      return next(new ApiError("Truyện không tồn tại", 404));
    }

    if (story.userId !== userId) {
      return next(
        new ApiError("Bạn không phải là tác giả của truyện này", 403)
      );
    }

    next();
  } catch (error) {
    return next(new ApiError("Lỗi khi kiểm tra quyền tác giả", 500));
  }
};

export const canAccessChapter = async (req, res, next) => {
  try {
    if (!req.user || !req.user.userId) {
      return next(new ApiError("Bạn phải đăng nhập để đọc truyện", 401));
    }

    const chapterId = req.params.chapterId;
    const userId = req.user.userId;

    const chapter = await models.Chapter.findByPk(chapterId, {
      include: [{ model: models.Story, as: "story" }],
    });

    if (!chapter) {
      return next(new ApiError("Chương không tồn tại", 404));
    }

    if (chapter.story.userId === userId) return next();
    if (!chapter.lockedStatus) return next();

    const storyPurchase = await models.Purchase.findOne({
      where: { userId, storyId: chapter.storyId, chapterId: null },
    });
    if (storyPurchase) return next();

    const chapterPurchase = await models.Purchase.findOne({
      where: { userId, chapterId },
    });
    if (chapterPurchase) return next();

    return next(new ApiError("Bạn cần mua chương này để truy cập", 403));
  } catch (error) {
    return next(new ApiError("Lỗi khi kiểm tra quyền truy cập chương", 500));
  }
};

export const canManageReadingList = async (req, res, next) => {
  try {
    const nameListId = req.params.nameListId || req.body.nameListId;
    const userId = req.user.userId;

    if (!nameListId) {
      return next(new ApiError("Thiếu nameListId", 400));
    }

    const nameList = await models.NameList.findByPk(nameListId);
    if (!nameList) {
      return next(new ApiError("Danh sách đọc không tồn tại", 404));
    }

    if (nameList.userId !== userId) {
      return next(
        new ApiError("Bạn không có quyền quản lý danh sách này", 403)
      );
    }

    next();
  } catch (error) {
    return next(new ApiError("Lỗi khi kiểm tra quyền quản lý danh sách", 500));
  }
};
