import { sequelize } from '../../models/index.js';
import { validateStory } from '../../utils/story.util.js';
import { Op } from 'sequelize';
import { handleTransaction } from '../../utils/handle_transaction.util.js';

const Chapter = sequelize.models.Chapter;
const StoryCategory = sequelize.models.StoryCategory;
const LikeComment = sequelize.models.LikeComment;
const Comment = sequelize.models.Comment;
const History = sequelize.models.History;
const Purchase = sequelize.models.Purchase;
const Vote = sequelize.models.Vote;
const ReadList = sequelize.models.ReadList;

const deleteStory = async (storyId, userId) => {
  return await handleTransaction(async (transaction) => {
    const story = await validateStory(storyId, userId);

    const chapters = await Chapter.findAll({
      where: { storyId },
      attributes: ['chapterId'],
      transaction,
    });
    const chapterIds = chapters.map((chapter) => chapter.chapterId);

    await LikeComment.destroy({
      where: {
        commentId: {
          [Op.in]: await Comment.findAll({
            where: { chapterId: { [Op.in]: chapterIds } },
            attributes: ['commentId'],
            transaction,
          }).map((comment) => comment.commentId),
        },
      },
      transaction,
    });

    await Comment.destroy({
      where: { chapterId: { [Op.in]: chapterIds } },
      transaction,
    });

    await History.destroy({
      where: { chapterId: { [Op.in]: chapterIds } },
      transaction,
    });

    await Purchase.destroy({
      where: { storyId },
      transaction,
    });

    await Chapter.destroy({
      where: { storyId },
      transaction,
    });

    await Vote.destroy({
      where: { storyId },
      transaction,
    });

    await StoryCategory.destroy({
      where: { storyId },
      transaction,
    });

    await ReadList.destroy({
      where: { storyId },
      transaction,
    });

    await story.destroy({ transaction });

    return { success: true, message: 'Xóa truyện thành công' };
  });
};

export default deleteStory;
