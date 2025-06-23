import express from 'express';
import * as communityController from '../controllers/community.controller.js';

const router = express.Router();

// Lấy tất cả cộng đồng
router.get('/', communityController.getAllCommunities);

// Lấy cộng đồng theo ID
router.get('/:communityId', communityController.getCommunityById);

// Tạo cộng đồng mới
router.post('/', communityController.createCommunity);

// Cập nhật cộng đồng
router.put('/:communityId', communityController.updateCommunity);

// lọc
router.put('/', communityController.filterCommunities);

// Xóa cộng đồng
router.delete('/:communityId', communityController.deleteCommunity);

export default router;