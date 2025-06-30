import { models, sequelize } from '../../models/index.js'
import ApiError from '../../utils/api_error.util.js'
import {
  validateStory,
  handleStoryPurchaseTransaction,
} from '../../utils/story.util.js'
import { handleTransaction } from '../../utils/handle_transaction.util.js'
import {
  validateChapter,
  handlePurchaseTransaction,
  checkChapterAccessCore,
} from '../../utils/chapter.util.js'
import { validateUser } from '../../utils/user.util.js'

const StoryInteractionService = {
  async purchaseChapter(userId, storyId, chapterId) {
    return await handleTransaction(async (transaction) => {
      const chapter = await validateChapter(chapterId, true)
      return await handlePurchaseTransaction(
        userId,
        chapter,
        storyId,
        transaction
      )
    })
  },

  async purchaseEntireStory(userId, storyId) {
    return await handleTransaction(async (transaction) => {
      return await handleStoryPurchaseTransaction(userId, storyId, transaction)
    })
  },

  async checkChapterAccess(userId, storyId, chapterId) {
    return await checkChapterAccessCore(userId, storyId, chapterId, true)
  },

  async checkStoryPurchaseStatus(userId, storyId) {
    try {
      await validateUser(userId)
      await validateStory(storyId)

      const purchase = await models.Purchase.findOne({
        where: {
          userId,
          storyId,
          chapterId: null, // Purchase của whole story
        },
      })

      return {
        isPurchased: !!purchase,
        purchasedAt: purchase?.purchasedAt || null,
      }
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi kiểm tra trạng thái mua truyện', 500)
    }
  },
}

export default StoryInteractionService
