import { models, sequelize } from '../models/index.js'
import ApiError from './api_error.util.js'
import { createNotification } from './notification.util.js'

export const validateChapter = async (chapterId, includeStory = false) => {
  try {
    const include = includeStory ? [{ model: models.Story, as: 'story' }] : []
    const chapter = await models.Chapter.findByPk(chapterId, { include })
    if (!chapter) throw new ApiError('Chương không tồn tại', 404)
    return chapter
  } catch (error) {
    throw error instanceof ApiError
      ? error
      : new ApiError('Lỗi khi kiểm tra chương', 500)
  }
}

export const checkChapterAccessCore = async (
  userId,
  storyId,
  chapterId,
  returnDetails = false
) => {
  try {
    const chapter = await models.Chapter.findByPk(chapterId, {
      include: [{ model: models.Story, as: 'story' }],
    })
    if (!chapter) throw new ApiError('Chương không tồn tại', 404)

    if (chapter.storyId !== parseInt(storyId)) {
      throw new ApiError('Chương không thuộc truyện này', 400)
    }

    if (!chapter.lockedStatus) {
      return returnDetails ? { purchased: true, isUnlocked: true } : true
    }

    if (chapter.story.userId === userId) {
      return returnDetails ? { purchased: true, isAuthor: true } : true
    }

    const storyPurchase = await models.Purchase.findOne({
      where: { userId, storyId, chapterId: null },
    })
    if (storyPurchase) {
      return returnDetails ? { purchased: true, isEntireStory: true } : true
    }

    const chapterPurchase = await models.Purchase.findOne({
      where: { userId, chapterId },
    })
    return returnDetails ? { purchased: !!chapterPurchase } : !!chapterPurchase
  } catch (error) {
    throw error instanceof ApiError
      ? error
      : new ApiError('Lỗi khi kiểm tra quyền truy cập chương', 500)
  }
}

export const canUserAccessChapter = async (userId, chapter) => {
  return await checkChapterAccessCore(
    userId,
    chapter.storyId,
    chapter.chapterId,
    false
  )
}

export const getChaptersByStory = async ({
  storyId,
  userId = null,
  limit = 20,
  lastId = null,
  orderBy = 'ordinalNumber',
  sort = 'ASC',
}) => {
  try {
    const validOrderFields = ['ordinalNumber', 'createdAt', 'updatedAt']
    const { orderBy: finalOrderBy, sort: finalSort } = validateSortParams(
      orderBy,
      sort,
      validOrderFields
    )

    const where = lastId
      ? {
          storyId,
          chapterId: {
            [finalSort === 'DESC'
              ? models.Sequelize.Op.lt
              : models.Sequelize.Op.gt]: lastId,
          },
        }
      : { storyId }

    const chapters = await models.Chapter.findAll({
      where,
      limit: parseInt(limit),
      order: [[finalOrderBy, finalSort]],
      include: [
        {
          model: models.Story,
          as: 'story',
          attributes: ['storyId', 'storyName', 'userId'],
        },
      ],
    })

    if (userId) {
      for (const chapter of chapters) {
        chapter.dataValues.canAccess = await canUserAccessChapter(
          userId,
          chapter
        )
      }
    }

    const nextLastId =
      chapters.length > 0 ? chapters[chapters.length - 1].chapterId : null
    return {
      chapters,
      nextLastId,
      hasMore: chapters.length === parseInt(limit),
    }
  } catch (error) {
    throw error instanceof ApiError
      ? error
      : new ApiError('Lỗi khi lấy danh sách chương', 500)
  }
}

export const handlePurchaseTransaction = async (
  userId,
  chapter,
  storyId,
  transaction
) => {
  try {
    const user = await models.User.findByPk(userId, { transaction })
    if (!user) throw new ApiError('Người dùng không tồn tại', 404)

    if (chapter.storyId !== parseInt(storyId)) {
      throw new ApiError('Chương không thuộc truyện này', 400)
    }

    if (chapter.story.userId === userId) {
      throw new ApiError('Bạn là tác giả, không cần mua chương', 400)
    }

    if (!chapter.lockedStatus) {
      throw new ApiError('Chương này không bị khóa', 400)
    }

    const existingPurchase = await models.Purchase.findOne({
      where: { userId, chapterId: chapter.chapterId },
      transaction,
    })
    if (existingPurchase) {
      throw new ApiError('Bạn đã mua chương này rồi', 400)
    }

    const story = chapter.story
    if (!story.pricePerChapter || story.pricePerChapter <= 0) {
      throw new ApiError('Chương này không có giá bán', 400)
    }

    if (user.wallet < story.pricePerChapter) {
      throw new ApiError('Số dư không đủ', 400)
    }

    await models.Purchase.create(
      {
        userId,
        storyId,
        chapterId: chapter.chapterId,
        purchasedAt: new Date(),
      },
      { transaction }
    )

    await user.update(
      { wallet: sequelize.literal(`wallet - ${story.pricePerChapter}`) },
      { transaction }
    )

    await models.Transaction.create(
      {
        userId,
        money: story.pricePerChapter,
        type: 'CHAPTER_PURCHASE',
        time: new Date(),
        status: 'COMPLETED',
        finishAt: new Date(),
      },
      { transaction }
    )

    const author = await models.User.findByPk(story.userId, { transaction })
    const authorPayment = story.pricePerChapter * 0.8

    await author.update(
      { wallet: sequelize.literal(`wallet + ${authorPayment}`) },
      { transaction }
    )

    await models.Transaction.create(
      {
        userId: author.userId,
        money: authorPayment,
        type: 'CHAPTER_SALE',
        time: new Date(),
        status: 'COMPLETED',
        finishAt: new Date(),
      },
      { transaction }
    )

    await createNotification({
      type: 'SALE',
      content: `Chương "${chapter.chapterName}" của truyện "${
        story.storyName
      }" đã được bán với giá ${authorPayment.toFixed(2)}`,
      refId: chapter.chapterId,
      userId: story.userId,
      transaction,
    })

    return {
      success: true,
      message: 'Mua chương thành công',
      purchasedChapterId: chapter.chapterId,
    }
  } catch (error) {
    throw error instanceof ApiError
      ? error
      : new ApiError('Lỗi khi mua chương', 500)
  }
}

// Hàm phụ trợ để chuẩn hóa tham số sắp xếp
export const validateSortParams = (orderBy, sort, validFields) => {
  const finalOrderBy = validFields.includes(orderBy) ? orderBy : validFields[0]
  const finalSort = ['ASC', 'DESC'].includes(sort) ? sort : 'ASC'
  return { orderBy: finalOrderBy, sort: finalSort }
}
