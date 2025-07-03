import { sequelize } from '../../models/index.js';
import ApiError from '../../utils/api_error.util.js';
import { validateUser } from '../../utils/user.util.js';
import ChatService from '../chat.service.js';
import { deleteImageOnCloudinary } from '../cloudinary.service.js';
import CommentService from '../comment.service.js';
import deleteStory from '../story/story_delete.service.js';

const deleteUser = async (userId) => {
  const t = await sequelize.transaction();
  try {
    const user = await validateUser(userId);

    if (user.avatarId) {
      await deleteImageOnCloudinary(user.avatarId);
    }
    if (user.backgroundId) {
      await deleteImageOnCloudinary(user.backgroundId);
    }

    const stories = await sequelize.models.Story.findAll({
      where: { userId },
      transaction: t,
    });
    for (const story of stories) {
      await deleteStory(story.storyId, { transaction: t });
    }

    const comments = await sequelize.models.Comment.findAll({
      where: { userId },
      transaction: t,
    });
    for (const comment of comments) {
      await CommentService.deleteComment(comment.commentId, { transaction: t });
    }                         

    const chats = await sequelize.models.Chat.findAll({
      where: { senderId: userId },
      transaction: t,
    });
    for (const chat of chats) {
      await ChatService.deleteChat(chat.chatId, { transaction: t });
    }

    // Xóa các bản ghi liên quan còn lại bằng SQL
    await sequelize.query(
      `
      DELETE FROM follow WHERE followId = :userId OR followedId = :userId;
      DELETE FROM read_list WHERE nameListId IN (SELECT nameListId FROM name_list WHERE userId = :userId);
      DELETE FROM name_list WHERE userId = :userId;
      DELETE FROM notification WHERE userId = :userId;
      DELETE FROM password_reset WHERE userId = :userId;
      DELETE FROM purchase WHERE userId = :userId;
      DELETE FROM transaction WHERE userId = :userId;
      DELETE FROM history WHERE userId = :userId;
      DELETE FROM premium WHERE userId = :userId;
      DELETE FROM vote WHERE userId = :userId;
      DELETE FROM join_community WHERE userId = :userId;
      DELETE FROM like_comment WHERE userId = :userId;
      DELETE FROM user WHERE userId = :userId;
    `,
      {
        replacements: { userId },
        type: sequelize.QueryTypes.RAW,
        transaction: t,
      }
    );

    await t.commit();
    return { message: 'Xóa người dùng thành công' };
  } catch (err) {
    await t.rollback();
    console.log(err)
    if (err instanceof ApiError){ ;throw err;}
    throw new ApiError('Lỗi khi xóa người dùng', 500);
  }
};

export default deleteUser;
