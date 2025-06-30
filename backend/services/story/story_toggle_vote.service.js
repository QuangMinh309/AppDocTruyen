import { sequelize } from '../../models/index.js';
import { validateStory } from '../../utils/story.util.js';
import { handleTransaction } from '../../utils/handle_transaction.util.js';

import { validateUser } from '../../utils/user.util.js';

const Vote = sequelize.models.Vote;
const Story = sequelize.models.Story;

const toggleVote = async (userId, storyId) => {
  return await handleTransaction(async (transaction) => {
    await validateUser(userId);
    await validateStory(storyId);

    const existingVote = await Vote.findOne({
      where: { userId, storyId },
      transaction,
    });

    let result;
    if (existingVote) {
      await existingVote.destroy({ transaction });
      await Story.update(
        { voteNum: sequelize.literal('voteNum - 1') },
        { where: { storyId }, transaction }
      );

      const updatedStory = await Story.findByPk(storyId, {
        transaction,
      });

      result = {
        message: 'Đã bỏ vote truyện',
        voteCount: updatedStory.voteNum,
        hasVoted: false,
      };
    } else {
      await Vote.create({ userId, storyId }, { transaction });
      await Story.update(
        { voteNum: sequelize.literal('voteNum + 1') },
        { where: { storyId }, transaction }
      );

      const updatedStory = await Story.findByPk(storyId, {
        transaction,
      });

      result = {
        message: 'Đã vote cho truyện',
        voteCount: updatedStory.voteNum,
        hasVoted: true,
      };
    }

    return result;
  });
};

export default toggleVote;
