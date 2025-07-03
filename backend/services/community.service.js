import { sequelize } from '../models/index.js';
import { Op } from 'sequelize';
import ApiError from '../utils/api_error.util.js';
import {
  getImageUrlFromCloudinary,
  uploadImageToCloudinary,
  deleteImageOnCloudinary,
} from './cloudinary.service.js';

const Community = sequelize.models.Community;
const Chat = sequelize.models.Chat;
const Category = sequelize.models.Category;
const User = sequelize.models.User;
const Follow = sequelize.models.Follow;
const JoinCommunity = sequelize.models.JoinCommunity;

export function normalizeString(str) {
  return str
    .normalize('NFD') // tách dấu
    .replace(/[\u0300-\u036f]/g, '') // xóa dấu
    .replace(/đ/g, 'd')
    .replace(/Đ/g, 'D')
    .toLowerCase();
}

const CommunityService = {
  getAllCommunities: async (limit) => {
    try {
      const communities = await Community.findAll({
        limit,
        include: [
          {
            model: Category,
            as: 'category',
            attributes: ['categoryId', 'categoryName'],
          },
        ],
      });
      return await Promise.all(
        communities.map(async (community) => {
          const communityData = community.toJSON();

          communityData.avatarUrl = await getImageUrlFromCloudinary(
            communityData.avatarId
          );
          delete communityData.avatarId;
          delete communityData.categoryId;
          return communityData;
        })
      );
    } catch (err) {
      console.error('Lỗi khi lấy danh sách cộng đồng:', err);
      throw new ApiError('Lỗi khi lấy danh sách cộng đồng', 500);
    }
  },

  filterCommunities: async (limit, filter = {}) => {
    try {
      const whereClause = {};

      // Lọc theo categoryId nếu có
      if (filter.categoryId) {
        whereClause.categoryId = filter.categoryId;
      }

      // Lọc theo memberNum nếu có giới hạn (ví dụ >=)
      if (filter.memberNum) {
        whereClause.menberNum = {
          [Op.gte]: filter.minMemberNum,
        };
      }

      const communities = await Community.findAll({
        limit,
        where: whereClause,
        include: [
          {
            model: Category,
            as: 'category',
            attributes: ['categoryId', 'categoryName'],
          },
        ],
      });

      const result = await Promise.all(
        communities.map(async (community) => {
          const data = community.toJSON();
          data.avatarUrl = await getImageUrlFromCloudinary(data.avatarId);
          delete data.avatarId;
          delete data.categoryId;
          return data;
        })
      );

      return result;
    } catch (err) {
      console.error('Lỗi khi lọc cộng đồng:', err);
      throw new ApiError('Lỗi khi lọc cộng đồng', 500);
    }
  },
  addMember: async (communityId, userId) => {
    try {
      const community = await Community.findByPk(communityId);
      const user = await User.findByPk(userId);
      // console.log("aada",JoinCommunity)
      const hasJoin = await JoinCommunity.findOne({
        where: {
          userId,
          communityId,
        },
      });
      if (!community || !user)
        throw new ApiError('community hoặc user khoogn tồn tại', 404);
      if (hasJoin) return true;

      await JoinCommunity.create({ userId, communityId });
      await community.update({ memberNum: community.memberNum + 1 });
      return true;
    } catch (err) {
      console.error('Lỗi khi lấy danh sách cộng đồng:', err);
      throw new ApiError('Lỗi khi lấy danh sách cộng đồng', 500);
    }
  },
  getCommunityById: async (communityId, userId) => {
    try {
      const community = await Community.findByPk(communityId, {
        include: [
          {
            model: Category,
            as: 'category',
            attributes: ['categoryId', 'categoryName'],
          },
          {
            model: User,
            as: 'members', // Tên alias từ association
            through: { attributes: [] }, // Loại bỏ các cột từ JoinCommunity nếu không cần
            attributes: ['userId', 'userName', 'dUserName', 'avatarId'], // Chọn các trường cần từ User
          },
        ],
      });
      // Tạo đối tượng mới để lưu kết quả
      const result = community.toJSON();
      if (!result) {
        throw new ApiError('Cộng đồng không tồn tại', 404);
      }
      result.avatarUrl = await getImageUrlFromCloudinary(community.avatarId);
      await Promise.all(
        result.members.map(async (data) => {
          // console.log("id: ", data.avatarId)
          data.avatarUrl = await getImageUrlFromCloudinary(data.avatarId);

          const existingFollow = await Follow.findOne({
            where: { followId: userId, followedId: data.userId },
          });

          data.isFollowed = existingFollow ? true : false;

          delete data.avatarId;
          return data;
        })
      );
      delete result.avatarId;
      delete result.categoryId;
      // console.log(result)
      return result;
    } catch (err) {
      console.error('Lỗi khi lấy cộng đồng:', err);
      throw new ApiError('Lỗi khi lấy cộng đồng', 500);
    }
  },

  searchCommunityMembersByName: async (communityId, searchTerm, userId) => {
    try {
      const normalizedSearch = normalizeString(searchTerm);

      const community = await Community.findByPk(communityId, {
        include: [
          {
            model: User,
            as: 'members',
            through: { attributes: [] },
            attributes: ['userId', 'userName', 'dUserName', 'avatarId'],
            where: {
              [Op.and]: [
                sequelize.where(
                  sequelize.fn('LOWER', sequelize.col('userName')),
                  {
                    [Op.like]: `%${normalizedSearch}%`,
                  }
                ),
              ],
            },
          },
        ],
      });

      if (!community) {
        throw new ApiError('Cộng đồng không tồn tại', 404);
      }

      const result = await Promise.all(
        community.members.map(async (user) => {
          const userData = user.toJSON();
          userData.avatarUrl = await getImageUrlFromCloudinary(
            userData.avatarId
          );

          const existingFollow = await Follow.findOne({
            where: { followId: userId, followedId: userData.userId },
          });

          userData.isFollowed = !!existingFollow;
          delete userData.avatarId;

          return userData;
        })
      );
      // console.log(result)
      return result;
    } catch (err) {
      console.error('Lỗi khi tìm kiếm thành viên cộng đồng:', err);
      throw new ApiError('Lỗi khi tìm kiếm thành viên cộng đồng', 500);
    }
  },

  createCommunity: async (data) => {
    try {
      if (!(await Category.findByPk(data.categoryId)))
        throw new ApiError('category không tồn tại', 404);

      let avatarId = null;

      if (file) {
        try {
          const uploadResult = await uploadImageToCloudinary(
            file.buffer,
            'communities'
          );
          avatarId = uploadResult.public_id;
        } catch (error) {
          throw new ApiError('Tải ảnh đại diện thất bại', 500);
        }
      }

      const community = await Community.create({
        ...data,
        avatarId,
      });
      const communityWithCategory = await Community.findByPk(
        community.communityId,
        {
          include: [
            {
              model: Category,
              as: 'category',
              attributes: ['categoryId', 'categoryName'],
            },
          ],
        }
      );
      const result = communityWithCategory.toJSON();
      delete result.categoryId;
      return result;
    } catch (err) {
      console.error('Lỗi khi tạo cộng đồng:', err);
      throw new ApiError('Lỗi khi tạo cộng đồng', 500);
    }
  },

  async updateCommunity(communityId, data, file) {
    const { communityName, categoryId, memberNum, description } = data;

    try {
      const community = await Community.findByPk(communityId);
      if (!community) {
        throw new ApiError('Cộng đồng không tồn tại', 404);
      }

      if (categoryId) {
        const category = await Category.findByPk(categoryId);
        if (!category) throw new ApiError('Category không tồn tại', 404);
      }

      let avatarId = community.avatarId;

      if (file) {
        try {
          if (avatarId) {
            // Xoá ảnh cũ
            await deleteImageOnCloudinary(avatarId);
          }

          // Upload ảnh mới lên Cloudinary
          const uploadResult = await uploadImageToCloudinary(
            file.buffer,
            'communities'
          );
          avatarId = uploadResult.public_id;
        } catch (error) {
          throw new ApiError('Tải ảnh đại diện lên thất bại', 500);
        }
      }

      // Cập nhật dữ liệu cộng đồng
      await community.update({
        communityName: communityName || community.communityName,
        categoryId: categoryId || community.categoryId,
        avatarId: avatarId,
        description: description || community.description,
        memberNum: memberNum || community.memberNum,
      });

      const updatedCommunity = await Community.findByPk(communityId, {
        include: ['category'],
      });

      const result = updatedCommunity.toJSON();
      delete result.categoryId;

      return result;
    } catch (err) {
      console.error('Lỗi khi cập nhật cộng đồng:', err);
      throw new ApiError('Lỗi khi cập nhật cộng đồng', 500);
    }
  },

  deleteCommunity: async (communityId) => {
    try {
      const community = await Community.findByPk(communityId);
      if (!community) {
        throw new ApiError('Cộng đồng không tồn tại', 404);
      }
      await Chat.destroy({
        where: { communityId },
      });
      await community.destroy();
      return await Community.findByPk(communityId, {
        include: [
          {
            model: Category,
            as: 'category',
            attributes: ['categoryId', 'categoryName'],
          },
        ],
      });
    } catch (err) {
      console.error('Lỗi khi xoá cộng đồng:', err);
      throw new ApiError('Lỗi khi xoá cộng đồng', 500);
    }
  },
};

export default CommunityService;
