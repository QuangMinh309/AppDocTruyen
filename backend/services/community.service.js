import { sequelize } from '../models/index.js'
import { Op } from 'sequelize';
import ApiError from '../utils/api_error.util.js'
import {
    getImageUrlFromCloudinary,
} from './cloudinary.service.js'

const Community = sequelize.models.Community
const Chat = sequelize.models.Chat
const Category = sequelize.models.Category
const History = sequelize.models.History
const User = sequelize.models.User



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
            return await Promise.all(communities.map(async (community) => {
                const communityData = community.toJSON();

                communityData.avatarUrl = await getImageUrlFromCloudinary(communityData.avatarId);
                delete communityData.avatarId;
                delete communityData.categoryId;
                return communityData;
            }));
        } catch (err) {
            console.error('Lỗi khi lấy danh sách cộng đồng:', err)
            throw new ApiError('Lỗi khi lấy danh sách cộng đồng', 500)
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
    getCommunityById: async (communityId) => {
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
                        attributes: ['userId', 'dUserName', "avatarId"], // Chọn các trường cần từ User
                    },
                ],
            });
            // Tạo đối tượng mới để lưu kết quả
            const result = community.toJSON();
            if (!result) {
                throw new ApiError('Cộng đồng không tồn tại', 404);
            }
            result.avatarUrl = await getImageUrlFromCloudinary(community.avatarId);
            await Promise.all(result.members.map(async (data) => {
                console.log("id: ", data.avatarId)
                data.avatarUrl = await getImageUrlFromCloudinary(data.avatarId);
                delete data.avatarId;
                return data;
            }));
            delete result.avatarId;
            delete result.categoryId;
            console.log(result)
            return result;
        } catch (err) {
            console.error('Lỗi khi lấy cộng đồng:', err)
            throw new ApiError('Lỗi khi lấy cộng đồng', 500)
        }
    },

    createCommunity: async (data) => {
        try {
            if (!Category.findByPk(data.categoryId))
                throw new ApiError('category không tồn tại', 404)

            const community = await Community.create(data);
            const communityWithCategory = await Community.findByPk(community.communityId, {
                include: [
                    {
                        model: Category,
                        as: 'category',
                        attributes: ['categoryId', 'categoryName'],
                    },
                ],
            });
            delete communityData.categoryId;
            return communityWithCategory.toJSON();
        } catch (err) {
            console.error('Lỗi khi tạo cộng đồng:', err);
            throw new ApiError('Lỗi khi tạo cộng đồng', 500);
        }
    },
    updateCommunity: async (communityId, data) => {
        const { communityName,
            categoryId,
            avatarId,
            memberNum,
            description
        } = data
        try {
            let community = await Community.findByPk(communityId)
            if (!community) {
                throw new ApiError('Cộng đồng không tồn tại', 404)
            }
            if (!Category.findByPk(categoryId))
                throw new ApiError('category không tồn tại', 404)

            await community.update({
                communityName: communityName || community.communityName,
                categoryId: categoryId || community.categoryId,
                avatarId: avatarId || community.avatarId,
                description: description || community.description,
                memberNum: memberNum || community.memberNum
            })
            const updatedCommunity = await Community.findByPk(communityId, {
                include: ['category'],
            });
            delete updatedCommunity.categoryId;
            return updatedCommunity.toJSON();
        } catch (err) {
            console.error('Lỗi khi cập nhật cộng đồng:', err)
            throw new ApiError('Lỗi khi cập nhật cộng đồng', 500)
        }
    },


    deleteCommunity: async (communityId) => {
        try {
            const community = await Community.findByPk(communityId)
            if (!community) {
                throw new ApiError('Cộng đồng không tồn tại', 404)
            }
            await Chat.destroy({
                where: { communityId }
            })
            await community.destroy()
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
            console.error('Lỗi khi xoá cộng đồng:', err)
            throw new ApiError('Lỗi khi xoá cộng đồng', 500)
        }
    }

}

export default CommunityService