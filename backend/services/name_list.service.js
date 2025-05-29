import { models } from '../models/index.js'
import ApiError from '../utils/apiError.js'
import { validateUser, validateStory } from '../utils/storyUtils.js'
import { handleTransaction } from '../utils/transactionUtils.js'
import { createNotification } from '../utils/notificationUtils.js'

const NameListService = {
  async addToReadingList(userId, storyId, nameListId) {
    return await handleTransaction(async (transaction) => {
      await validateUser(userId)
      await validateStory(storyId)
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

      const existingEntry = await models.ReadList.findOne({
        where: { storyId, nameListId },
        transaction,
      })
      if (existingEntry) {
        throw new ApiError('Truyện đã có trong danh sách đọc này', 400)
      }

      await models.ReadList.create({ storyId, nameListId }, { transaction })

      const story = await models.Story.findByPk(storyId, { transaction })
      if (nameList.isPublic && story.userId !== userId) {
        await createNotification({
          type: 'READING_LIST',
          content: `Một người dùng đã thêm truyện "${story.storyName}" vào danh sách đọc của họ`,
          refId: story.storyId,
          userId: story.userId,
          transaction,
        })
      }

      return { success: true, message: 'Đã thêm truyện vào danh sách đọc' }
    })
  },

  async removeFromReadingList(userId, storyId, nameListId) {
    return await handleTransaction(async (transaction) => {
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
      if (deleted === 0) {
        throw new ApiError('Truyện không có trong danh sách đọc này', 404)
      }

      return { success: true, message: 'Đã xóa truyện khỏi danh sách đọc' }
    })
  },

  async createReadingList(userId, nameListData) {
    return await handleTransaction(async (transaction) => {
      await validateUser(userId)
      const nameList = await models.NameList.create(
        {
          nameList: nameListData.nameList,
          userId,
          description: nameListData.description || '',
          isPublic: nameListData.isPublic || false,
        },
        { transaction }
      )

      if (
        nameListData.stories &&
        Array.isArray(nameListData.stories) &&
        nameListData.stories.length > 0
      ) {
        const storyEntries = nameListData.stories.map((storyId) => ({
          storyId,
          nameListId: nameList.nameListId,
        }))
        await models.ReadList.bulkCreate(storyEntries, {
          transaction,
          ignoreDuplicates: true,
        })
      }

      return nameList
    })
  },

  async getReadingList(nameListId, { limit = 20, lastId = null } = {}) {
    try {
      const nameList = await models.NameList.findByPk(nameListId)
      if (!nameList) throw new ApiError('Danh sách đọc không tồn tại', 404)

      const where = lastId
        ? { nameListId, storyId: { [models.Sequelize.Op.lt]: lastId } }
        : { nameListId }

      const readList = await models.ReadList.findAll({
        where,
        limit: parseInt(limit),
        include: [
          {
            model: models.Story,
            as: 'story',
            include: [
              {
                model: models.User,
                as: 'author',
                attributes: ['userId', 'userName', 'avatarId'],
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

      const stories = readList.map((item) => item.story)
      const nextLastId =
        readList.length > 0 ? readList[readList.length - 1].storyId : null

      return {
        nameList: {
          nameListId: nameList.nameListId,
          nameList: nameList.nameList,
          description: nameList.description,
          isPublic: nameList.isPublic,
        },
        stories,
        nextLastId,
        hasMore: readList.length === parseInt(limit),
      }
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi lấy danh sách truyện', 500)
    }
  },

  async getUserReadingLists(userId, { limit = 20, lastId = null } = {}) {
    try {
      await validateUser(userId)
      const where = {
        userId,
        ...(lastId ? { nameListId: { [models.Sequelize.Op.lt]: lastId } } : {}),
      }

      const nameLists = await models.NameList.findAll({
        where,
        limit: parseInt(limit),
        include: [
          {
            model: models.Story,
            as: 'stories',
            through: { attributes: [] },
            attributes: ['storyId', 'storyName', 'coverImgId'],
            limit: 3,
          },
        ],
        order: [['nameListId', 'DESC']],
      })

      const nextLastId =
        nameLists.length > 0 ? nameLists[nameLists.length - 1].nameListId : null

      return {
        readingLists: nameLists.map((list) => ({
          nameListId: list.nameListId,
          nameList: list.nameList,
          description: list.description,
          isPublic: list.isPublic,
          storyCount: list.stories.length,
          previewStories: list.stories,
        })),
        nextLastId,
        hasMore: nameLists.length === parseInt(limit),
      }
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi lấy danh sách đọc của người dùng', 500)
    }
  },
}

export default NameListService
