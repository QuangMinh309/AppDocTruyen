import express from 'express';
import { authenticate } from '../middlewares/auth.middleware.js';
import validate from '../middlewares/validate.middleware.js';
import * as communityController from '../controllers/community.controller.js';
import { validateSearchMember } from '../validators/community.validation.js';
import { uploadSingleImage } from '../middlewares/uploadImage.middleware.js';

const router = express.Router();

router.use(authenticate);
// Lấy tất cả cộng đồng
router.get('/', communityController.getAllCommunities);

// Lấy cộng đồng theo ID
router.get('/:communityId', communityController.getCommunityById);

// Tìm kiếm thành viên cảu cộng đồng theo tên
router.get(
  '/search/:communityId',
  validate(validateSearchMember),
  communityController.searchCommunityMembersByName
);

// Tạo cộng đồng mới
router.post(
  '/',
  uploadSingleImage('avatarId'),
  communityController.createCommunity
);

// Cập nhật cộng đồng
router.put(
  '/:communityId',
  uploadSingleImage('avatarId'),
  communityController.updateCommunity
);

// lọc
router.put('/', communityController.filterCommunities);

// Xóa cộng đồng
router.delete('/:communityId', communityController.deleteCommunity);

export default router;
