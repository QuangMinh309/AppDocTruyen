import { sequelize } from '../../models/index.js';
import { validateStory } from '../../utils/story.util.js';
import { Op } from 'sequelize';
import { handleTransaction } from '../../utils/handle_transaction.util.js';
import { deleteImageOnCloudinary } from '../cloudinary.service.js';
import ApiError from '../../utils/api_error.util.js';
import ChapterService from '../chapter/chapter.service.js';

const Chapter = sequelize.models.Chapter;
const StoryCategory = sequelize.models.StoryCategory;
const Vote = sequelize.models.Vote;
const ReadList = sequelize.models.ReadList;

const deleteStory = async (storyId, userId) => {
  return await handleTransaction(async (transaction) => {
    const story = await validateStory(storyId, userId);

    if (story.coverImgId) {
      try {
        await deleteImageOnCloudinary(story.coverImgId);
      } catch (error) {
        throw new ApiError('Xóa ảnh bìa thất bại', 500);
      }
    }

    const chapters = await Chapter.findAll({
      where: { storyId },
      attributes: ['chapterId'],
      transaction,
    });
    const chapterIds = chapters.map((chapter) => chapter.chapterId);

    if (chapterIds.length > 0) {
      for (const chapterId of chapterIds) {
        await ChapterService.deleteChapter(chapterId, userId, transaction);
      }
    }

    await Promise.all([
      Vote.destroy({ where: { storyId }, transaction }),
      StoryCategory.destroy({ where: { storyId }, transaction }),
      ReadList.destroy({ where: { storyId }, transaction }),
    ]);

    await story.destroy({ transaction });

    return {
      success: true,
      message: 'Xóa truyện thành công',
    };
  });
};

export default deleteStory;
