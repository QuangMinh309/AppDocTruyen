import { sequelize } from '../models/index.js';
import ApiError from '../utils/api_error.util.js';
import { getImageUrlFromCloudinary, uploadBase64ToCloudinary, deleteImageOnCloudinary } from './cloudinary.service.js';


const getCommentImageData = async (commentJson) => {
    try {
        if (commentJson.user && commentJson.user.avatarId) {

            commentJson.user.avatarUrl = await getImageUrlFromCloudinary(commentJson.user.avatarId);
            delete commentJson.user.avatarId;
        }
    } catch (cloudinaryErr) {
        commentJson.user.avatarUrl = null;
        console.error(`Lỗi khi lấy dữ liệu hình ảnh từ Cloudinary: ${cloudinaryErr.message}`);
    }

    try {
        if (commentJson.commentPicId) {
            commentJson.commentPicUrl = await getImageUrlFromCloudinary(commentJson.commentPicId);
            delete commentJson.commentPicId;
        }
    } catch (cloudinaryErr) {
        commentJson.commentPicUrl = null;
        console.error(`Lỗi khi lấy dữ liệu hình ảnh từ Cloudinary: ${cloudinaryErr.message}`);

    }
    return commentJson;
}

const CommentService = {
    async getCommentById(commentId) {
        try {
            const commentData = await sequelize.models.Comment.findOne({
                where: { commentId },
                include: [
                    { model: sequelize.models.Chapter, as: 'chapter' },
                    { model: sequelize.models.User, as: 'user', attributes: ['userId', 'dUserName', 'avatarId'] },
                ],
            });

            const likeNumber = await sequelize.models.LikeComment.count({
                where: { commentId },
            });

            if (!commentData) {
                throw new ApiError('Không tìm thấy cuộc trò chuyện', 404);
            }
            const commentJson = await getCommentImageData(commentData.toJSON());
            return { ...commentJson, likeNumber };
        } catch (err) {
            if (err instanceof ApiError) throw err;
            throw new ApiError(`Lỗi khi lấy cuộc trò chuyện: ${err.message}`, 500);
        }
    },

    async getAllCommentsOfChapter(id) {
        try {
            if (!id || isNaN(id)) {
                throw new ApiError('chapterId không hợp lệ', 400);
            }
            console.log(`Fetching comments for chapterId: ${id}`);
            const comments = await sequelize.models.Comment.findAll({
                where: { chapterId: id },
                include: [
                    { model: sequelize.models.Chapter, as: 'chapter' },
                    { model: sequelize.models.User, as: 'user', attributes: ['userId', 'dUserName', 'avatarId'] },
                ],
            });


            // Lấy tất cả commentId
            const commentIds = comments.map(comment => comment.toJSON().commentId);
            if (commentIds.length === 0) {
                throw new ApiError('Không có bình luận nào cho chương này', 404);
            }

            console.log(`Found ${commentIds.length} comments for chapterId: ${id}`);
            // Truy vấn số lượt thích một lần duy nhất
            const likeCounts = await sequelize.models.LikeComment.findAll({
                attributes: ['commentId', [sequelize.fn('COUNT', '*'), 'likeNumber']],
                where: { commentId: commentIds },
                group: ['commentId'],
            });

            // Tạo map để tra cứu likeNumber
            const likeCountMap = likeCounts.reduce((map, row) => {
                map[row.commentId] = parseInt(row.get('likeNumber'), 10);
                return map;
            }, {});

            // Xử lý comments mà không cần await trong map
            const commentsWithLikes = comments.map(async (comment) => {
                let commentJson = comment.toJSON();
                commentJson = await getCommentImageData(commentJson);
                if (!commentJson.commentId) {
                    console.error("commentId is missing in commentJson:", commentJson);
                    return { ...commentJson, likeNumber: 0 };
                }
                const likeNumber = likeCountMap[commentJson.commentId] || 0;
                return { ...commentJson, likeNumber };
            });

            return await Promise.all(commentsWithLikes);
        } catch (err) {
            console.error('Error in getAllCommentsOfChapter:', err.message);
            if (err instanceof ApiError) throw err;
            throw new ApiError(`Lỗi khi lấy danh sách cuộc trò chuyện: ${err.message}`, 500);
        }
    },

    async createComment(data) {
        try {
            console.log('Creating comment with data:', data);
            if (!data.chapterId || (!data.content && !data.commentPicData)) {
                throw new ApiError('Thiếu các trường bắt buộc', 400);
            }
            let publicId = null;
            let commentPicUrl = null;
            if (data.commentPicData) {
                const result = await uploadBase64ToCloudinary(data.commentPicData, "user/community/comment");
                publicId = result.public_id;
                commentPicUrl = result.secure_url;

            }
            const commentData = await sequelize.models.Comment.create({ ...data, commentPicId: publicId });
            const fullComment = await sequelize.models.Comment.findByPk(commentData.commentId, {
                include: [
                    { model: sequelize.models.Chapter, as: 'chapter' },
                    { model: sequelize.models.User, as: 'user', attributes: ['userId', 'dUserName', 'avatarId'] },
                ],
            });

            const commentJson = await getCommentImageData(fullComment.toJSON());
            console.log('Created comment JSON:', commentJson);
            return { comment: { ...commentJson, likeNumber: 0 } }; // Mặc định likeNumber là 0 khi tạo mới
        } catch (err) {
            if (err instanceof ApiError) throw err;
            throw new ApiError(`Lỗi khi tạo cuộc trò chuyện: ${err.message}`, 500);
        }
    },

    async updateComment(commentId, data) {
        try {
            if (!data.content && !data.commentPicData) {
                throw new ApiError('Thiếu nội dung tin nhắn', 400);
            }
            const commentData = await sequelize.models.Comment.findByPk(commentId);
            if (!commentData) {
                throw new ApiError('Cuộc trò chuyện không tồn tại', 404);
            }
            let publicId = null;
            let commentPicUrl = null;
            if (data.commentPicData) {
                const result = await uploadBase64ToCloudinary(data.commentPicData, "user/community/chat");
                publicId = result.public_id;
                commentPicUrl = result.secure_url;

            }
            await commentData.update({ ...data, commentPicId: publicId });
            const updatedComment = await sequelize.models.Comment.findByPk(commentId, {
                include: [
                    { model: sequelize.models.Chapter, as: 'chapter' },
                    { model: sequelize.models.User, as: 'user', attributes: ['userId', 'dUserName', 'avatarId'] },
                ],
            });
            const commentJson = await getCommentImageData(updatedComment.toJSON());
            const likeNumber = await sequelize.models.LikeComment.count({
                where: { commentId },
            });
            return { comment: { ...commentJson, likeNumber } };
        } catch (err) {
            if (err instanceof ApiError) throw err;
            throw new ApiError(`Lỗi khi cập nhật cuộc trò chuyện: ${err.message}`, 500);
        }
    },

    async deleteComment(commentId) {
        try {
            const commentData = await sequelize.models.Comment.findByPk(commentId);
            if (!commentData) {
                throw new ApiError('Cuộc trò chuyện không tồn tại', 404);
            }
            // Xoá tất cả lượt thích liên quan đến comment này
            await sequelize.models.LikeComment.destroy({
                where: { commentId },
            });
            // Xoá hình ảnh nếu có
            if (commentData.commentPicId) {
                try {
                    await deleteImageOnCloudinary(commentData.commentPicId);
                } catch (cloudinaryErr) {
                    console.error(`Lỗi khi xoá hình ảnh từ Cloudinary: ${cloudinaryErr.message}`);
                    throw new ApiError(`Lỗi khi xoá hình ảnh: ${cloudinaryErr.message}`, 500);
                }
            }
            await commentData.destroy();
            return { message: 'Xoá comment thành công', commentId };
        } catch (err) {
            if (err instanceof ApiError) throw err;
            throw new ApiError(`Lỗi khi xoá cuộc trò chuyện: ${err.message}`, 500);
        }
    },

    async likeComment(userId, commentId) {
        try {
            const comment = await sequelize.models.Comment.findByPk(commentId);
            if (!comment) {
                throw new ApiError('Comment không tồn tại', 404);
            }

            const existingLike = await sequelize.models.LikeComment.findOne({
                where: { userId, commentId },
            });

            if (existingLike) {
                throw new ApiError('Bạn đã thích bình luận này rồi', 400);
            }

            await sequelize.models.LikeComment.create({ userId, commentId });
            const likeCount = await sequelize.models.LikeComment.count({
                where: { commentId },
            });
            const commentData = await sequelize.models.Comment.findByPk(commentId, {
                include: [
                    { model: sequelize.models.Chapter, as: 'chapter' },
                    { model: sequelize.models.User, as: 'user', attributes: ['userId', 'dUserName', 'avatarId'] },
                ],
            });
            const commentJson = await getCommentImageData(commentData.toJSON());
            return { comment: { ...commentJson, likeCount } };
        } catch (err) {
            if (err instanceof ApiError) throw err;
            throw new ApiError(`Lỗi khi thích bình luận: ${err.message}`, 500);
        }
    },

    async unlikeComment(userId, commentId) {
        try {
            const comment = await sequelize.models.Comment.findByPk(commentId);
            if (!comment) {
                throw new ApiError('Comment không tồn tại', 404);
            }

            const existingLike = await sequelize.models.LikeComment.findOne({
                where: { userId, commentId },
            });

            if (existingLike) {
                await existingLike.destroy();
            }

            await sequelize.models.LikeComment.create({ userId, commentId });
            const likeCount = await sequelize.models.LikeComment.count({
                where: { commentId },
            });
            const commentData = await sequelize.models.Comment.findByPk(commentId, {
                include: [
                    { model: sequelize.models.Chapter, as: 'chapter' },
                    { model: sequelize.models.User, as: 'user', attributes: ['userId', 'dUserName', 'avatarId'] },
                ],
            });
            const commentJson = await getCommentImageData(commentData.toJSON());
            return { comment: { ...commentJson, likeCount } };
        } catch (err) {
            if (err instanceof ApiError) throw err;
            throw new ApiError(`Lỗi khi thích bình luận: ${err.message}`, 500);
        }
    },
};

export default CommentService;