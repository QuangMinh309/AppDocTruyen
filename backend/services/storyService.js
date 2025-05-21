import { models, sequelize } from "../models/index.js";
import ApiError from "../utils/apiError.js";
import {
  validateStory,
  validateUser,
  validateCategory,
  validateSortParams,
  handleStoryPurchaseTransaction,
  updateStoryViewNum,
} from "../utils/storyUtils.js";
import { handleTransaction } from "../utils/transactionUtils.js";
import { createNotification } from "../utils/notificationUtils.js";
import {
  validateChapter,
  handlePurchaseTransaction,
  checkChapterAccessCore,
} from "../utils/chapterUtils.js";
import { uploadImageToCloudinary } from "./cloudinaryService.js";

const StoryService = {
  async createStory(storyData, userId) {
    return await handleTransaction(async (transaction) => {
      let coverImgId = null;
      if (storyData.coverImg) {
        const uploadResult = await uploadImageToCloudinary(storyData.coverImg);
        coverImgId = uploadResult.public_id;
      }

      const story = await models.Story.create(
        {
          ...storyData,
          userId,
          viewNum: 0,
          voteNum: 0,
          chapterNum: 0,
          coverImgId,
        },
        { transaction }
      );

      if (storyData.categories && storyData.categories.length > 0) {
        const categoryAssociations = storyData.categories.map((categoryId) => ({
          storyId: story.storyId,
          categoryId,
        }));
        await models.StoryCategory.bulkCreate(categoryAssociations, {
          transaction,
        });
      }

      return story;
    });
  },

  async getStoryById(storyId) {
    try {
      const story = await models.Story.findByPk(storyId, {
        include: [
          {
            model: models.User,
            as: "author",
            attributes: ["userId", "userName", "avatarId", "dUserName"],
          },
          {
            model: models.Chapter,
            as: "chapters",
            attributes: [
              "chapterId",
              "chapterName",
              "ordinalNumber",
              "updatedAt",
              "lockedStatus",
            ],
          },
          {
            model: models.Category,
            as: "categories",
            attributes: ["categoryId", "categoryName"],
            through: { attributes: [] },
          },
        ],
      });
      if (!story) throw new ApiError("Truyện không tồn tại", 404);

      // Cập nhật lượt xem
      await updateStoryViewNum(storyId);

      return story;
    } catch (err) {
      throw err instanceof ApiError
        ? err
        : new ApiError("Lỗi khi lấy truyện", 500);
    }
  },

  async updateStory(storyId, storyData, userId) {
    return await handleTransaction(async (transaction) => {
      const story = await validateStory(storyId, userId);
      const img = storyData.coverImg
        ? await uploadImageToCloudinary(storyData.coverImg, "stories/covers")
        : { public_id: story.coverImgId, url: story.coverImgUrl };

      await story.update(
        {
          ...storyData,
          coverImgId: img.public_id,
          coverImgUrl: img.url,
        },
        { transaction }
      );

      if (storyData.categories && Array.isArray(storyData.categories)) {
        await models.StoryCategory.destroy({ where: { storyId }, transaction });
        if (storyData.categories.length > 0) {
          const categoryAssociations = storyData.categories.map(
            (categoryId) => ({
              storyId,
              categoryId,
            })
          );
          await models.StoryCategory.bulkCreate(categoryAssociations, {
            transaction,
          });
        }
      }

      return story;
    });
  },

  async deleteStory(storyId, userId) {
    return await handleTransaction(async (transaction) => {
      await validateStory(storyId, userId);

      // Xóa các comment liên quan đến các chapter của story
      await models.LikeComment.destroy({
        where: {
          commentId: {
            [models.Sequelize.Op.in]: sequelize.literal(
              `(SELECT commentId FROM Comments WHERE chapterId IN (SELECT chapterId FROM Chapters WHERE storyId = ${storyId}))`
            ),
          },
        },
        transaction,
      });
      await models.Comment.destroy({
        where: {
          chapterId: {
            [models.Sequelize.Op.in]: sequelize.literal(
              `(SELECT chapterId FROM Chapters WHERE storyId = ${storyId})`
            ),
          },
        },
        transaction,
      });

      // Xóa các dữ liệu liên quan đến chapter và story
      await Promise.all([
        models.History.destroy({
          where: {
            chapterId: {
              [models.Sequelize.Op.in]: sequelize.literal(
                `(SELECT chapterId FROM Chapters WHERE storyId = ${storyId})`
              ),
            },
          },
          transaction,
        }),
        models.Chapter.destroy({ where: { storyId }, transaction }),
        models.Vote.destroy({ where: { storyId }, transaction }),
        models.StoryCategory.destroy({ where: { storyId }, transaction }),
        models.ReadList.destroy({ where: { storyId }, transaction }),
        models.Purchase.destroy({ where: { storyId }, transaction }),
        models.Story.destroy({ where: { storyId }, transaction }),
      ]);

      return { success: true, message: "Xóa truyện thành công" };
    });
  },

  async getAllStories({
    limit = 20,
    lastId = null,
    orderBy = "createdAt",
    sort = "ASC",
  } = {}) {
    try {
      const validOrderFields = ["createdAt", "updatedAt", "voteNum", "viewNum"];
      const { orderBy: finalOrderBy, sort: finalSort } = validateSortParams(
        orderBy,
        sort,
        validOrderFields
      );

      const where = lastId
        ? {
            storyId: {
              [finalSort === "DESC"
                ? models.Sequelize.Op.lt
                : models.Sequelize.Op.gt]: lastId,
            },
          }
        : {};

      const stories = await models.Story.findAll({
        where,
        limit: parseInt(limit),
        include: [
          {
            model: models.User,
            as: "author",
            attributes: ["userId", "userName", "avatarId", "dUserName"],
          },
          {
            model: models.Category,
            as: "categories",
            attributes: ["categoryId", "categoryName"],
            through: { attributes: [] },
          },
        ],
        order: [[finalOrderBy, finalSort]],
      });

      const nextLastId =
        stories.length > 0 ? stories[stories.length - 1].storyId : null;
      return {
        stories,
        nextLastId,
        hasMore: stories.length === parseInt(limit),
      };
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError("Lỗi khi lấy danh sách truyện", 500);
    }
  },

  async filterByCategory(
    categoryId,
    { limit = 20, lastId = null, orderBy = "createdAt", sort = "DESC" } = {}
  ) {
    try {
      await validateCategory(categoryId);
      const validOrderFields = ["createdAt", "updatedAt", "voteNum", "viewNum"];
      const { orderBy: finalOrderBy, sort: finalSort } = validateSortParams(
        orderBy,
        sort,
        validOrderFields
      );

      const where = lastId
        ? {
            storyId: {
              [finalSort === "DESC"
                ? models.Sequelize.Op.lt
                : models.Sequelize.Op.gt]: lastId,
            },
          }
        : {};

      const stories = await models.Story.findAll({
        where,
        limit: parseInt(limit),
        include: [
          {
            model: models.User,
            as: "author",
            attributes: ["userId", "userName", "avatarId"],
          },
          {
            model: models.Category,
            as: "categories",
            where: { categoryId },
            attributes: ["categoryId", "categoryName"],
            through: { attributes: [] },
          },
        ],
        order: [[finalOrderBy, finalSort]],
      });

      const nextLastId =
        stories.length > 0 ? stories[stories.length - 1].storyId : null;
      return {
        stories,
        nextLastId,
        hasMore: stories.length === parseInt(limit),
      };
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError("Lỗi khi lọc truyện theo thể loại", 500);
    }
  },

  async filterByCategoryAndStatus(
    categoryId,
    status,
    { limit = 20, lastId = null, orderBy = "createdAt", sort = "DESC" } = {}
  ) {
    try {
      await validateCategory(categoryId);
      const validStatuses = ["ongoing", "completed", "hiatus", "cancelled"];
      if (!validStatuses.includes(status)) {
        throw new ApiError("Trạng thái truyện không hợp lệ", 400);
      }

      const validOrderFields = ["createdAt", "updatedAt", "voteNum", "viewNum"];
      const { orderBy: finalOrderBy, sort: finalSort } = validateSortParams(
        orderBy,
        sort,
        validOrderFields
      );

      const where = {
        status,
        ...(lastId
          ? {
              storyId: {
                [finalSort === "DESC"
                  ? models.Sequelize.Op.lt
                  : models.Sequelize.Op.gt]: lastId,
              },
            }
          : {}),
      };

      const stories = await models.Story.findAll({
        where,
        limit: parseInt(limit),
        include: [
          {
            model: models.User,
            as: "author",
            attributes: ["userId", "userName", "avatarId"],
          },
          {
            model: models.Category,
            as: "categories",
            where: { categoryId },
            attributes: ["categoryId", "categoryName"],
            through: { attributes: [] },
          },
        ],
        order: [[finalOrderBy, finalSort]],
      });

      const nextLastId =
        stories.length > 0 ? stories[stories.length - 1].storyId : null;
      return {
        stories,
        nextLastId,
        hasMore: stories.length === parseInt(limit),
      };
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError("Lỗi khi lọc truyện theo thể loại và trạng thái", 500);
    }
  },

  async filterByVote({ limit = 20, lastId = null } = {}) {
    try {
      const where = lastId
        ? { storyId: { [models.Sequelize.Op.lt]: lastId } }
        : {};

      const stories = await models.Story.findAll({
        where,
        limit: parseInt(limit),
        include: [
          {
            model: models.User,
            as: "author",
            attributes: ["userId", "userName", "avatarId"],
          },
          {
            model: models.Category,
            as: "categories",
            attributes: ["categoryId", "categoryName"],
            through: { attributes: [] },
          },
        ],
        order: [
          ["voteNum", "DESC"],
          ["storyId", "DESC"],
        ],
      });

      const nextLastId =
        stories.length > 0 ? stories[stories.length - 1].storyId : null;
      return {
        stories,
        nextLastId,
        hasMore: stories.length === parseInt(limit),
      };
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError("Lỗi khi lọc truyện theo số vote", 500);
    }
  },

  async filterByUpdateDate({ limit = 20, lastId = null } = {}) {
    try {
      const where = lastId
        ? { storyId: { [models.Sequelize.Op.lt]: lastId } }
        : {};

      const stories = await models.Story.findAll({
        where,
        limit: parseInt(limit),
        include: [
          {
            model: models.User,
            as: "author",
            attributes: ["userId", "userName", "avatarId"],
          },
          {
            model: models.Category,
            as: "categories",
            attributes: ["categoryId", "categoryName"],
            through: { attributes: [] },
          },
        ],
        order: [
          ["updatedAt", "DESC"],
          ["storyId", "DESC"],
        ],
      });

      const nextLastId =
        stories.length > 0 ? stories[stories.length - 1].storyId : null;
      return {
        stories,
        nextLastId,
        hasMore: stories.length === parseInt(limit),
      };
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError("Lỗi khi lọc truyện theo ngày cập nhật", 500);
    }
  },

  async filterByUser(
    userId,
    { limit = 20, lastId = null, includeAll = false } = {}
  ) {
    try {
      await validateUser(userId);
      const where = {
        userId,
        ...(lastId ? { storyId: { [models.Sequelize.Op.lt]: lastId } } : {}),
      };

      const stories = await models.Story.findAll({
        where,
        limit: parseInt(limit),
        include: [
          {
            model: models.User,
            as: "author",
            attributes: ["userId", "userName", "avatarId"],
          },
          {
            model: models.Category,
            as: "categories",
            attributes: ["categoryId", "categoryName"],
            through: { attributes: [] },
          },
          ...(includeAll
            ? [
                {
                  model: models.Chapter,
                  as: "chapters",
                  attributes: [
                    "chapterId",
                    "chapterName",
                    "ordinalNumber",
                    "updatedAt",
                  ],
                },
              ]
            : []),
        ],
        order: [
          ["createdAt", "DESC"],
          ["storyId", "DESC"],
        ],
      });

      const nextLastId =
        stories.length > 0 ? stories[stories.length - 1].storyId : null;
      return {
        stories,
        nextLastId,
        hasMore: stories.length === parseInt(limit),
      };
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError("Lỗi khi lọc truyện theo người dùng", 500);
    }
  },

  async searchStories(searchTerm, { limit = 20, lastId = null } = {}) {
    const { Op } = models.Sequelize;
    try {
      const where = {
        storyName: { [Op.like]: `%${searchTerm}%` },
        ...(lastId ? { storyId: { [models.Sequelize.Op.lt]: lastId } } : {}),
      };

      const stories = await models.Story.findAll({
        where,
        limit: parseInt(limit),
        include: [
          {
            model: models.User,
            as: "author",
            attributes: ["userId", "userName", "avatarId"],
          },
          {
            model: models.Category,
            as: "categories",
            attributes: ["categoryId", "categoryName"],
            through: { attributes: [] },
          },
        ],
        order: [
          ["createdAt", "DESC"],
          ["storyId", "DESC"],
        ],
      });

      const nextLastId =
        stories.length > 0 ? stories[stories.length - 1].storyId : null;
      return {
        stories,
        nextLastId,
        hasMore: stories.length === parseInt(limit),
      };
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError("Lỗi khi tìm kiếm truyện", 500);
    }
  },

  async getRecentlyReadStories(userId, { limit = 20, lastId = null } = {}) {
    try {
      await validateUser(userId);
      const where = {
        userId,
        ...(lastId
          ? {
              chapterId: { [models.Sequelize.Op.lt]: lastId },
              storyId: {
                [models.Sequelize.Op.in]: sequelize.literal(
                  `(SELECT storyId FROM Chapters WHERE chapterId < ${lastId})`
                ),
              },
            }
          : {}),
      };

      const histories = await models.History.findAll({
        where,
        limit: parseInt(limit),
        attributes: ["chapterId"],
        include: [
          {
            model: models.Chapter,
            as: "chapter",
            attributes: ["storyId"],
            required: true,
            include: [
              {
                model: models.Story,
                as: "story",
                attributes: ["storyId", "storyName", "coverImgId", "status"],
                include: [
                  {
                    model: models.User,
                    as: "author",
                    attributes: ["userId", "userName", "avatarId"],
                  },
                  {
                    model: models.Category,
                    as: "categories",
                    attributes: ["categoryId", "categoryName"],
                    through: { attributes: [] },
                  },
                ],
              },
            ],
          },
        ],
        order: [
          [models.sequelize.literal("lastReadAt"), "DESC"],
          ["chapterId", "DESC"],
        ],
      });

      const stories = histories.map((h) => h.chapter.story);
      const nextLastId =
        histories.length > 0 ? histories[histories.length - 1].chapterId : null;

      return {
        stories,
        nextLastId,
        hasMore: histories.length === parseInt(limit),
      };
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError("Lỗi khi lấy truyện đã đọc gần đây", 500);
    }
  },

  async getPurchasedStories(userId, { limit = 20, lastId = null } = {}) {
    try {
      await validateUser(userId);
      const where = {
        userId,
        chapterId: null,
        ...(lastId ? { storyId: { [models.Sequelize.Op.lt]: lastId } } : {}),
      };

      const purchases = await models.Purchase.findAll({
        where,
        limit: parseInt(limit),
        attributes: ["storyId"],
        include: [
          {
            model: models.Story,
            as: "story",
            attributes: ["storyId", "storyName", "coverImgId", "status"],
            include: [
              {
                model: models.User,
                as: "author",
                attributes: ["userId", "userName", "avatarId"],
              },
              {
                model: models.Category,
                as: "categories",
                attributes: ["categoryId", "categoryName"],
                through: { attributes: [] },
              },
            ],
          },
        ],
        order: [
          [models.sequelize.literal("purchasedAt"), "DESC"],
          ["storyId", "DESC"],
        ],
      });

      const stories = purchases.map((p) => p.story);
      const nextLastId =
        purchases.length > 0 ? purchases[purchases.length - 1].storyId : null;

      return {
        stories,
        nextLastId,
        hasMore: purchases.length === parseInt(limit),
      };
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError("Lỗi khi lấy truyện đã mua", 500);
    }
  },

  async toggleVote(userId, storyId) {
    return await handleTransaction(async (transaction) => {
      await validateUser(userId);
      await validateStory(storyId);

      const existingVote = await models.Vote.findOne({
        where: { userId, storyId },
        transaction,
      });

      let result;
      if (existingVote) {
        await existingVote.destroy({ transaction });
        await models.Story.update(
          { voteNum: sequelize.literal("voteNum - 1") },
          { where: { storyId }, transaction }
        );
        result = {
          action: "removed",
          message: "Vote đã được xóa khỏi truyện",
          voteCount: (await models.Story.findByPk(storyId, { transaction }))
            .voteNum,
        };
      } else {
        await models.Vote.create({ userId, storyId }, { transaction });
        await models.Story.update(
          { voteNum: sequelize.literal("voteNum + 1") },
          { where: { storyId }, transaction }
        );
        const story = await models.Story.findByPk(storyId, { transaction });
        if (story.userId !== userId) {
          await createNotification({
            type: "VOTE",
            content: `Một người dùng đã vote cho truyện "${story.storyName}" của bạn`,
            refId: story.storyId,
            userId: story.userId,
            transaction,
          });
        }
        result = {
          action: "added",
          message: "Vote đã được thêm vào truyện",
          voteCount: story.voteNum + 1,
        };
      }

      return result;
    });
  },

  async checkVoteStatus(userId, storyId) {
    try {
      await validateUser(userId);
      await validateStory(storyId);
      const vote = await models.Vote.findOne({
        where: { userId, storyId },
      });
      return { hasVoted: !!vote };
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError("Lỗi khi kiểm tra trạng thái vote", 500);
    }
  },

  async purchaseChapter(userId, storyId, chapterId) {
    return await handleTransaction(async (transaction) => {
      const chapter = await validateChapter(chapterId, true);
      return await handlePurchaseTransaction(
        userId,
        chapter,
        storyId,
        transaction
      );
    });
  },

  async purchaseEntireStory(userId, storyId) {
    return await handleTransaction(async (transaction) => {
      return await handleStoryPurchaseTransaction(userId, storyId, transaction);
    });
  },

  async checkChapterPurchase(userId, storyId, chapterId) {
    return await checkChapterAccessCore(userId, storyId, chapterId, true);
  },
};

export default StoryService;
