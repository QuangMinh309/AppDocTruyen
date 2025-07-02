import { checkChapterAccessCore } from '../../utils/chapter.util.js';

const StoryInteractionService = {
  async checkChapterAccess(userId, storyId, chapterId) {
    return await checkChapterAccessCore(userId, storyId, chapterId, true);
  },
};

export default StoryInteractionService;
