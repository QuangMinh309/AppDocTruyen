import Community from '../models/entities/community.js'
import ApiError from '../utils/api_error.util.js'


const CommunityService = {


    getAllCommunities: async (limit = 20) => {
        try {
            const communities = await Community.findAll({ limit })
            return communities.map((community) => community.toJSON())
        } catch (err) {
            console.error('Lỗi khi lấy danh sách cộng đồng:', err)
            throw new ApiError('Lỗi khi lấy danh sách cộng đồng', 500)
        }
    },

    getCommunityById: async (communityId) => {
        try {
            const community = await Community.findByPk(communityId)
            if (!community) {
                throw new ApiError('Cộng đồng không tồn tại', 404)
            }
            return community.toJSON()
        } catch (err) {
            console.error('Lỗi khi lấy cộng đồng:', err)
            throw new ApiError('Lỗi khi lấy cộng đồng', 500)
        }
    },

    createCommunity: async (data) => {
        try {
            const community = await Community.create(data)
            return community.toJSON()
        } catch (err) {
            console.error('Lỗi khi tạo cộng đồng:', err)
            throw new ApiError('Lỗi khi tạo cộng đồng', 500)
        }
    },

    updateCommunity: async (communityId, data) => {
        try {
            const community = await Community.findByPk(communityId)
            if (!community) {
                throw new ApiError('Cộng đồng không tồn tại', 404)
            }
            await community.update(data)
            return community.toJSON()
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
            await community.destroy()
            return { message: 'Xoá cộng đồng thành công' }
        } catch (err) {
            console.error('Lỗi khi xoá cộng đồng:', err)
            throw new ApiError('Lỗi khi xoá cộng đồng', 500)
        }
    },

}

export default CommunityService