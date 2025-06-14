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
  async getRecentlyReadStories(userId, { limit = 20, lastId = null } = {}) {
    try {
      await validateUser(userId)

      const whereClause = {
        userId,
      }

      if (lastId) {
        whereClause.chapterId = {
          [models.Sequelize.Op.lt]: lastId,
        }
      }

      const histories = await models.History.findAll({
        where: whereClause,
        include: [
          {
            model: models.Chapter,
            as: 'chapter',
            attributes: ['chapterId', 'chapterName', 'storyId'],
            required: true,
            include: [
              {
                model: models.Story,
                as: 'story',
                attributes: ['storyId', 'storyName', 'coverImgId', 'status'],
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
          },
        ],
        order: [
          ['lastReadAt', 'DESC'],
          ['chapterId', 'DESC'],
        ],
        limit: parseInt(limit),
      })

      // Lọc trùng truyện
      const uniqueStories = []
      const seenStoryIds = new Set()

      for (const history of histories) {
        const story = history.chapter?.story
        if (!story) continue

        const storyId = story.storyId
        if (!seenStoryIds.has(storyId)) {
          seenStoryIds.add(storyId)
          uniqueStories.push({
            ...story.toJSON(),
            lastReadChapter: history.chapter.chapterName,
            lastReadAt: history.lastReadAt,
          })
        }
      }

      const nextLastId =
        histories.length > 0 ? histories[histories.length - 1].chapterId : null

      return {
        stories: uniqueStories,
        nextLastId,
        hasMore: histories.length === parseInt(limit),
      }
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi lấy truyện đã đọc gần đây', 500)
    }
  },

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

  async getStoriesFromReadList(
    userId,
    nameListId,
    { limit = 20, lastId = null } = {}
  ) {
    try {
      await validateUser(userId)

      // Kiểm tra nameList có thuộc về user không
      const nameList = await models.NameList.findOne({
        where: { nameListId, userId },
      })

      if (!nameList) {
        throw new ApiError(
          'Danh sách đọc không tồn tại hoặc không thuộc về bạn',
          404
        )
      }

      const where = lastId
        ? { storyId: { [models.Sequelize.Op.lt]: lastId } }
        : {}

      const readListEntries = await models.ReadList.findAll({
        where: {
          nameListId,
          ...where,
        },
        limit: parseInt(limit),
        include: [
          {
            model: models.Story,
            as: 'story', // Cần định nghĩa association này trong model ReadList
            attributes: ['storyId', 'storyName', 'coverImgId', 'status'],
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
        order: [['storyId', 'DESC']],
      })

      const stories = readListEntries.map((entry) => entry.story)
      const nextLastId =
        readListEntries.length > 0
          ? readListEntries[readListEntries.length - 1].storyId
          : null

      return {
        stories,
        nextLastId,
        hasMore: readListEntries.length === parseInt(limit),
        readListInfo: {
          nameListId: nameList.nameListId,
          nameList: nameList.nameList,
          description: nameList.description,
        },
      }
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi lấy truyện từ danh sách đọc', 500)
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
          action: 'removed',
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
          action: 'added',
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

  async addToReadList(userId, storyId, nameListId) {
    return await handleTransaction(async (transaction) => {
      await validateUser(userId)
      await validateStory(storyId)

      // Kiểm tra nameList có thuộc về user không
      const nameList = await models.NameList.findOne({
        where: { nameListId, userId },
        transaction,
      })

      if (!nameList) {
        throw new ApiError(
          'Danh sách đọc không tồn tại hoặc không thuộc về bạn',
          404
        )
      }

      // Kiểm tra đã có trong danh sách chưa
      const existingEntry = await models.ReadList.findOne({
        where: { storyId, nameListId },
        transaction,
      })

      if (existingEntry) {
        throw new ApiError('Truyện đã có trong danh sách đọc', 409)
      }

      await models.ReadList.create({ storyId, nameListId }, { transaction })

      return {
        success: true,
        message: 'Đã thêm truyện vào danh sách đọc',
      }
    })
  },

  async removeFromReadList(userId, storyId, nameListId) {
    return await handleTransaction(async (transaction) => {
      await validateUser(userId)
      await validateStory(storyId)

      // Kiểm tra nameList có thuộc về user không
      const nameList = await models.NameList.findOne({
        where: { nameListId, userId },
        transaction,
      })

      if (!nameList) {
        throw new ApiError(
          'Danh sách đọc không tồn tại hoặc không thuộc về bạn',
          404
        )
      }

      const deleted = await models.ReadList.destroy({
        where: { storyId, nameListId },
        transaction,
      })

      if (!deleted) {
        throw new ApiError('Truyện không có trong danh sách đọc', 404)
      }

      return {
        success: true,
        message: 'Đã xóa truyện khỏi danh sách đọc',
      }
    })
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
