import { sequelize } from '../models/index.js';
import { handleTransaction } from '../utils/handle_transaction.util.js';
import { validateStory } from '../utils/story.util.js';
import { formatDate } from '../utils/date.util.js';
import { validateUser } from '../utils/user.util.js';
import ApiError from '../utils/api_error.util.js';

const NameList = sequelize.models.NameList;
const ReadList = sequelize.models.ReadList;
const Story = sequelize.models.Story;
const User = sequelize.models.User;
const Category = sequelize.models.Category;

const NameListService = {
  async createReadingList(userId, nameListData) {
    try {
      return await handleTransaction(async (transaction) => {
        await validateUser(userId);

        const { nameList, description, stories } = nameListData;

        const newNameList = await NameList.create(
          {
            nameList,
            userId,
            description: description || '',
          },
          { transaction }
        );

        if (Array.isArray(stories) && stories.length > 0) {
          const storyEntries = stories.map((storyId) => ({
            storyId,
            nameListId: newNameList.nameListId,
          }));
          await ReadList.bulkCreate(storyEntries, {
            transaction,
            ignoreDuplicates: true,
          });
        }

        return {
          ...newNameList.toJSON(),
        };
      });
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi tạo danh sách đọc', 500);
    }
  },

  async updateReadingList(userId, nameListId, updateData) {
    try {
      return await handleTransaction(async (transaction) => {
        const nameList = await NameList.findOne({
          where: { nameListId, userId },
          transaction,
        });
        if (!nameList) {
          throw new ApiError(
            'Danh sách đọc không tồn tại hoặc không thuộc về bạn',
            404
          );
        }

        await nameList.update(updateData, { transaction });

        return {
          ...nameList.toJSON(),
        };
      });
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi cập nhật danh sách đọc', 500);
    }
  },

  async getReadingList(nameListId, { limit = 20, lastId = null }) {
    try {
      const nameList = await NameList.findByPk(nameListId);
      if (!nameList) throw new ApiError('Danh sách đọc không tồn tại', 404);

      const stories = await nameList.getStories({
        where: lastId
          ? { storyId: { [sequelize.Sequelize.Op.lt]: lastId } }
          : {},
        include: [
          {
            model: User,
            as: 'author',
            attributes: ['userId', 'userName', 'avatarId'],
          },
          {
            model: Category,
            as: 'categories',
            attributes: ['categoryId', 'categoryName'],
            through: { attributes: [] },
          },
        ],
        through: { attributes: [] },
        order: [['storyId', 'DESC']],
        limit: parseInt(limit),
      });

      return {
        nameList: nameList.toJSON(),
        stories: stories.map((story) => {
          const plain = story.toJSON();
          delete plain.ReadList;
          return {
            ...plain,
            createdAt: formatDate(plain.createdAt),
            updatedAt: formatDate(plain.updatedAt),
          };
        }),
        nextLastId:
          stories.length > 0 ? stories[stories.length - 1].storyId : null,
        hasMore: stories.length === parseInt(limit),
      };
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi lấy danh sách truyện', 500);
    }
  },

  async getUserReadingLists(userId, { limit = 20, lastId = null }) {
    try {
      await validateUser(userId);

      const where = lastId
        ? { userId, nameListId: { [sequelize.Sequelize.Op.lt]: lastId } }
        : { userId };

      const nameLists = await NameList.findAll({
        where,
        limit: parseInt(limit),
        include: [
          {
            model: Story,
            as: 'stories',
            through: { attributes: [] },
            attributes: ['coverImgId'],
          },
        ],
        order: [['nameListId', 'DESC']],
      });

      return {
        readingLists: nameLists.map((list) => ({
          ...list.toJSON(),
          storyCount: list.stories.length,
          stories: list.stories.slice(0, 3).map((story) => ({
            ...story.toJSON(),
          })),
        })),
        nextLastId:
          nameLists.length > 0
            ? nameLists[nameLists.length - 1].nameListId
            : null,
        hasMore: nameLists.length === parseInt(limit),
      };
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi lấy danh sách đọc của người dùng', 500);
    }
  },

  async addToReadingList(userId, storyId, nameListId) {
    try {
      return await handleTransaction(async (transaction) => {
        await validateUser(userId);
        await validateStory(storyId);

        const nameList = await NameList.findOne({
          where: { nameListId, userId },
          transaction,
        });
        if (!nameList) {
          throw new ApiError(
            'Danh sách đọc không tồn tại hoặc không thuộc về bạn',
            404
          );
        }

        const existingEntry = await ReadList.findOne({
          where: { storyId, nameListId },
          transaction,
        });
        if (existingEntry) {
          throw new ApiError('Truyện đã có trong danh sách đọc này', 400);
        }

        const readListEntry = await ReadList.create(
          { storyId, nameListId },
          { transaction }
        );

        return {
          ...readListEntry.toJSON(),
        };
      });
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi thêm truyện vào danh sách', 500);
    }
  },

  async removeFromReadingList(userId, storyId, nameListId) {
    try {
      return await handleTransaction(async (transaction) => {
        const nameList = await NameList.findOne({
          where: { nameListId, userId },
          transaction,
        });
        if (!nameList) {
          throw new ApiError(
            'Danh sách đọc không tồn tại hoặc không thuộc về bạn',
            404
          );
        }

        const deleted = await ReadList.destroy({
          where: { storyId, nameListId },
          transaction,
        });
        if (deleted === 0) {
          throw new ApiError('Truyện không có trong danh sách đọc này', 404);
        }

        return { storyId, nameListId };
      });
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi xóa truyện khỏi danh sách', 500);
    }
  },

  async deleteReadingList(userId, nameListId) {
    try {
      return await handleTransaction(async (transaction) => {
        const nameList = await NameList.findOne({
          where: { nameListId, userId },
          transaction,
        });
        if (!nameList) {
          throw new ApiError(
            'Danh sách đọc không tồn tại hoặc không thuộc về bạn',
            404
          );
        }

        await ReadList.destroy({ where: { nameListId }, transaction });
        await nameList.destroy({ transaction });

        return { nameListId };
      });
    } catch (error) {
      throw error instanceof ApiError
        ? error
        : new ApiError('Lỗi khi xóa danh sách đọc', 500);
    }
  },
};

export default NameListService;
