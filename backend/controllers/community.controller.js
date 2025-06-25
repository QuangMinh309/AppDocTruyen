import CommunityService from '../services/community.service.js';
import ApiError from '../utils/api_error.util.js';


// Lấy tất cả cộng đồng
export const getAllCommunities = async (req, res, next) => {
    try {
        const limit = parseInt(req.query.limit) || 30; // Lấy limit từ query parameter, mặc định 20
        const communities = await CommunityService.getAllCommunities(limit);
        res.status(200).json(communities);

    } catch (error) {
        next(error); // Chuyển lỗi sang middleware xử lý lỗi
    }
};
export const filterCommunities = async (req, res, next) => {
    try {
        const limit = parseInt(req.query.limit) || 30; // Lấy limit từ query parameter, mặc định 20
        const communities = await CommunityService.filterCommunities(limit, req.body);
        res.status(200).json(communities);

    } catch (error) {
        next(error); // Chuyển lỗi sang middleware xử lý lỗi
    }
}

// Lấy cộng đồng theo ID
export const getCommunityById = async (req, res, next) => {
    try {
        const community = await CommunityService.getCommunityById(req.params.communityId);
        res.status(200).json(community);
    } catch (error) {
        next(error);
    }
};

// Tạo cộng đồng mới
export const createCommunity = async (req, res, next) => {
    try {
        // Nếu không phải admin, không được tạo mới cộng đồng
        if (req.user.role.roleName !== 'admin') {
            throw new ApiError("Bạn không có quyền tạo cộng đồng")
        }
        const data = req.body;
        const community = await CommunityService.createCommunity(data);
        res.status(201).json(community);
    } catch (error) {
        next(error);
    }
};

// Cập nhật cộng đồng
export const updateCommunity = async (req, res, next) => {
    try {
        // Nếu không phải admin, không được cập nhật cộng đồng
        if (req.user.role.roleName !== 'admin') {
            throw new ApiError("Bạn không có quyền cập nhật cộng đồng")
        }
        const communityId = req.params.communityId;
        const data = req.body;
        const community = await CommunityService.updateCommunity(communityId, data);
        res.status(200).json(community);
    } catch (error) {
        next(error);
    }
};

// Xóa cộng đồng
export const deleteCommunity = async (req, res, next) => {
    try {
        // Nếu không phải admin, không được xóa cộng đồng
        if (req.user.role.roleName !== 'admin') {
            throw new ApiError("Bạn không có quyền xóa cộng đồng")
        }
        const communityId = req.params.communityId;
        const result = await CommunityService.deleteCommunity(communityId);
        res.status(200).json(result);
    } catch (error) {
        next(error);
    }
};