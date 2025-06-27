import { models, sequelize } from '../../models/index.js'
import ApiError from '../../utils/api_error.util.js'
import {
  validateStory,
  handleStoryPurchaseTransaction,
} from '../../utils/story.util.js'
import { handleTransaction } from '../../utils/handle_transaction.util.js'
import NotificationService from '../notification.service.js'
import {
  validateChapter,
  handlePurchaseTransaction,
  checkChapterAccessCore,
} from '../../utils/chapter.util.js'
import { validateUser } from '../../utils/user.util.js'

const StoryInteractionService = {
  async getPurchasedStories(userId, { limit = 20, lastId = null } = {}) {
    try {
      await validateUser(userId)
      const where = {
        userId,
        chapterId: null, // Chỉ lấy purchase của story, không phải chapter
        ...(lastId ? { storyId: { [models.Sequelize.Op.lt]: lastId } } : {}),
      }

      const purchases = await models.Purchase.findAll({
        where,
        limit: parseInt(limit),
        attributes: ['storyId', 'purchasedAt'],
        include: [
          {
            model: models.Story,
            as: 'story',
            attributes: [
              'storyId',
              'storyName',
              'coverImgId',
              'status',
              'price',
            ],
            include: [
              {
                model: models.User,
                as: 'author',
                attributes: ['userId', 'userName', 'avatarId', 'dUserName'],
              },
              {
                model: models.Category,
                as: 'categories',
                attributes: ['categoryId', 'categoryName'],
                through: { attributes: [] },
              },
            ],
          },
        ],
        order: [
          ['purchasedAt', 'DESC'],
          ['storyId', 'DESC'],
        ],
      })

      const stories = purchases.map((p) => ({
        ...p.story.toJSON(),
        purchasedAt: p.purchasedAt,
      }))

      const nextLastId =
        purchases.length > 0 ? purchases[purchases.length - 1].storyId : null

      return {
        stories,
        nextLastId,
        hasMore: purchases.length === parseInt(limit),
      }
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi lấy truyện đã mua', 500)
    }
  },

  async toggleVote(userId, storyId) {
    return await handleTransaction(async (transaction) => {
      await validateUser(userId)
      const story = await validateStory(storyId)

      const existingVote = await models.Vote.findOne({
        where: { userId, storyId },
        transaction,
      })

      let result
      if (existingVote) {
        await existingVote.destroy({ transaction })
        await models.Story.update(
          { voteNum: sequelize.literal('voteNum - 1') },
          { where: { storyId }, transaction }
        )

        const updatedStory = await models.Story.findByPk(storyId, {
          transaction,
        })

        result = {
          message: 'Đã bỏ vote truyện',
          voteCount: updatedStory.voteNum,
          hasVoted: false,
        }
      } else {
        await models.Vote.create({ userId, storyId }, { transaction })
        await models.Story.update(
          { voteNum: sequelize.literal('voteNum + 1') },
          { where: { storyId }, transaction }
        )

        const updatedStory = await models.Story.findByPk(storyId, {
          transaction,
        })

        // Tạo notification nếu không phải tác giả vote cho truyện của mình
        if (story.userId !== userId) {
          await NotificationService.createNotification({
            type: 'VOTE',
            content: `Một người dùng đã vote cho truyện "${story.storyName}" của bạn`,
            refId: story.storyId,
            userId: story.userId,
            transaction,
          })
        }

        result = {
          message: 'Đã vote cho truyện',
          voteCount: updatedStory.voteNum,
          hasVoted: true,
        }
      }

      return result
    })
  },

  async checkVoteStatus(userId, storyId) {
    try {
      await validateUser(userId)
      await validateStory(storyId)

      const vote = await models.Vote.findOne({
        where: { userId, storyId },
      })

      const story = await models.Story.findByPk(storyId, {
        attributes: ['voteNum'],
      })

      return {
        hasVoted: !!vote,
        voteCount: story.voteNum,
      }
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi kiểm tra trạng thái vote', 500)
    }
  },

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
