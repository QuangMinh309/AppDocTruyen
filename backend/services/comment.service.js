import { sequelize } from '../models/index.js';
import ApiError from '../utils/api_error.util.js';
import { getImageUrlFromCloudinary, uploadBase64ToCloudinary, deleteImageOnCloudinary } from './cloudinary.service.js';
import moment from 'moment-timezone'; // Use moment-timezone for timezone support
import getStoryById from './story/story_get_id.service.js';
import ChapterService from './chapter/chapter.service.js';


const getCommentImageData = async (commentJson) => {
    try {
        if (commentJson.user) {
            commentJson.user.avatarUrl = await getImageUrlFromCloudinary(commentJson.user.avatarId);
            delete commentJson.user.avatarId;
        }
    } catch (cloudinaryErr) {
        commentJson.user.avatarUrl = "";
        console.error(`Lỗi khi lấy dữ liệu hình ảnh từ Cloudinary: ${cloudinaryErr.message}`);
    }

    try {
        commentJson.commentPicUrl = await getImageUrlFromCloudinary(commentJson.commentPicId);
        delete commentJson.commentPicId;
    } catch (cloudinaryErr) {
        commentJson.commentPicUrl = null;
        console.error(`Lỗi khi lấy dữ liệu hình ảnh từ Cloudinary: ${cloudinaryErr.message}`);

    }
    return commentJson;
}

const CommentService = {

    async getAllCommentsOfStory(id) {
        try {
            const story = getStoryById(id)
            if (!story) throw new ApiError(`Truyện này không tồn tại!`, 500);;

            const comments = await sequelize.models.Comment.findAll({
            limit: 20,
            attributes: {
                include: [
                [fn('COUNT', col('likedBy.userId')), 'likeNum']
                ]
            },
            include: [
                {
                model: sequelize.models.Chapter,
                as: 'chapter',
                where: { storyId: id },
                attributes: []
                },
                {
                model: sequelize.models.User,
                as: 'user',
                attributes: ['userId', 'dUserName', 'avatarId']
                },
                {
                model: sequelize.models.User,
                as: 'likedBy',
                attributes: [], // Không cần dữ liệu, chỉ cần đếm
                through: { attributes: [] } // Không cần thông tin bảng trung gian
                }
            ],
            group: ['Comment.commentId', 'user.userId'],
            order: [[fn('COUNT', col('likedBy.userId')), 'DESC']]
            });


            const commentPromises = comments.map(comment => {
                const commentJson = comment.toJSON();
              

                // Convert time to UTC+7 instead of UTC
                commentJson.time = moment(commentJson.time).tz('Asia/Ho_Chi_Minh').format(); // e.g., 2025-06-26T15:24:45+07:00

                return getCommentImageData(commentJson);
            });
            return await Promise.all(commentPromises);
        } catch (err) {
            if (err instanceof ApiError) throw err;
            throw new ApiError(`Lỗi khi lấy danh sách comment: ${err.message}`, 500);
        }
    },
     async getAllCommentsOfChapter(id) {
        try {
            const story = ChapterService.getChapterById(id)
            if (!story) throw new ApiError(`Truyện này không tồn tại!`, 500);;

            const comments = await sequelize.models.Comment.findAll({
                  where: { communityId: id },
                attributes: {
                    include: [
                    [fn('COUNT', col('likedBy.userId')), 'likeNum']
                    ]
                },
                include: [
                    {
                    model: sequelize.models.User,
                    as: 'user',
                    attributes: ['userId', 'dUserName', 'avatarId']
                    },
                    {
                    model: sequelize.models.User,
                    as: 'likedBy',
                    attributes: [], // Không cần dữ liệu, chỉ cần đếm
                    through: { attributes: [] } // Không cần thông tin bảng trung gian
                    }
            ],
            group: ['Comment.commentId', 'user.userId'],
            order: [[fn('COUNT', col('likedBy.userId')), 'DESC']]
            });


            const commentPromises = comments.map(comment => {
                const commentJson = comment.toJSON();

                // Convert time to UTC+7 instead of UTC
                commentJson.time = moment(commentJson.time).tz('Asia/Ho_Chi_Minh').format(); // e.g., 2025-06-26T15:24:45+07:00

                return getCommentImageData(commentJson);
            });
            return await Promise.all(commentPromises);
        } catch (err) {
            if (err instanceof ApiError) throw err;
            throw new ApiError(`Lỗi khi lấy danh sách comment: ${err.message}`, 500);
        }
    },
    

    async createComment(data, userId) {
        try {
            if (!data.content && !data.commentPicData) {
                throw new ApiError('Không được để trống nội dung!', 400);
            }
            let publicId = null;
            let commentPicUrl = null;
            if (data.commentPicData) {
                const result = await uploadBase64ToCloudinary(data.commentPicData, "user/story/comment");
                publicId = result.public_id;
                commentPicUrl = result.secure_url;

            }
            data.time = new Date()
            const commentData = await sequelize.models.Comment.create({ ...data, commentPicId: publicId });
            const fullComment = await sequelize.models.Comment.findByPk(commentData.commentId, {
                include: [

                    { model: sequelize.models.User, as: 'user', attributes: ['userId', 'dUserName', 'avatarId'] },
                ],
            });
            const commentJson = fullComment.toJSON();
            // Convert time to UTC+7 instead of UTC
            commentJson.time = moment(commentJson.time).tz('Asia/Ho_Chi_Minh').format(); // e.g., 2025-06-26T15:24:45+07:00
            console.log('Full comment data:', commentJson);
            return getCommentImageData(commentJson);
        } catch (err) {
            if (err instanceof ApiError) throw err;
            throw new ApiError(`Lỗi khi tạo comment: ${err.message}`, 500);
        }
    },

    async updateComment(commentId, data, userId) {
        try {
            if (!data.content && !data.commentPicData) {
                throw new ApiError('Không có nội dng cần cập nhật!', 400);
            }
            const commentData = await sequelize.models.Comment.findByPk(commentId);
            if (!commentData) {
                throw new ApiError('comment không tồn tại', 404);
            }
            let publicId = null;
            let commentPicUrl = null;
            if (data.commentPicData) {
                const result = await uploadBase64ToCloudinary(data.commentPicData, "user/story/comment");
                publicId = result.public_id;
                commentPicUrl = result.secure_url;

            }
            await commentData.update({ ...data, commentPicId: publicId });
            const updatedComment = await sequelize.models.Comment.findByPk(commentId, {
                include: [
                    { model: sequelize.models.User, as: 'user', attributes: ['userId', 'dUserName', 'avatarId'] },
                ],
            });

            const commentJson = fullComment.toJSON();
            // Convert time to UTC+7 instead of UTC
            commentJson.time = moment(commentJson.time).tz('Asia/Ho_Chi_Minh').format(); // e.g., 2025-06-26T15:24:45+07:00
            console.log('Full comment data:', commentJson);
            return getCommentImageData(commentJson);
        } catch (err) {
            if (err instanceof ApiError) throw err;
            throw new ApiError(`Lỗi khi cập nhật comment: ${err.message}`, 500);
        }
    },

    async deleteComment(commentId) {
        try {
            const commentData = await sequelize.models.Comment.findByPk(commentId);
            if (!commentData) {
                throw new ApiError('comment không tồn tại', 404);
            }
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
            throw new ApiError(`Lỗi khi xoá comment: ${err.message}`, 500);
        }
    },
};

export default CommentService;