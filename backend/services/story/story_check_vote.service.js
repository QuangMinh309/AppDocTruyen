import { models, sequelize } from '../../models/index.js';
import ApiError from '../../utils/api_error.util.js';
import { validateStory } from '../../utils/story.util.js';
import { validateUser } from '../../utils/user.util.js';

const checkVoteStatus = async (userId, storyId) => {
  try {
    await validateUser(userId);
    await validateStory(storyId);

    const vote = await models.Vote.findOne({
      where: { userId, storyId },
    });

    const story = await models.Story.findByPk(storyId, {
      attributes: ['voteNum'],
    });

    return {
      hasVoted: !!vote,
      voteCount: story.voteNum,
    };
  } catch (error) {
    throw error instanceof ApiError
      ? error
      : new ApiError('Lỗi khi kiểm tra trạng thái vote', 500);
  }
};

export default checkVoteStatus;
