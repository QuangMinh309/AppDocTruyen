import { sequelize } from '../models/index.js';
import ApiError from '../utils/api_error.util.js';
import { getImageUrlFromCloudinary, uploadBase64ToCloudinary, deleteImageOnCloudinary } from './cloudinary.service.js';
import moment from 'moment-timezone'; // Use moment-timezone for timezone support
import getStoryById from './story/story_get_id.service.js';
import ChapterService from './chapter/chapter.service.js';
import { fn, col } from 'sequelize';



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
                attributes: ['chapterName']
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
     async getAllCommentsOfChapter(id,userId) {
        try {
            // console.log(id)
            const chapter = ChapterService.getChapterById(id)
            if (!chapter) throw new ApiError(`chap này không tồn tại!`, 500);;

           const comments = await sequelize.models.Comment.findAll({
                limit: 20,
                where: { chapterId: id },
                attributes: {
                    include: [
                    [sequelize.literal(`(
                        SELECT COUNT(*)
                        FROM like_comment AS lc
                        WHERE lc.commentId = Comment.commentId
                    )`), 'likeNum']
                    ]
                },
                include: [
                    {
                    model: sequelize.models.User,
                    as: 'user',
                    attributes: ['userId', 'dUserName', 'avatarId']
                    },
                  {
                    model: sequelize.models.Chapter,
                    as: 'chapter', attributes: ['chapterId', 'chapterName', 'storyId','ordinalNumber','lockedStatus'],

                    }
                ],
                order: [[sequelize.literal('likeNum'), 'DESC']]
            });


            const commentPromises = comments.map(async(comment) => {
                const commentJson = comment.toJSON();
                const userLike = await sequelize.models.LikeComment.findOne({
                    where: {userId: userId, commentId: commentJson.commentId}
                    });
                commentJson.isUserLike = (userLike)? true : false;
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
            data.createAt = new Date()
            const commentData = await sequelize.models.Comment.create({ ...data, commentPicId: publicId });
            const fullComment = await sequelize.models.Comment.findByPk(commentData.commentId, {
                include: [

                    { model: sequelize.models.User, as: 'user', attributes: ['userId', 'dUserName', 'avatarId'] },
                    {
                    model: sequelize.models.Chapter,
                    as: 'chapter', attributes: ['chapterId', 'chapterName', 'storyId','ordinalNumber','lockedStatus'],

                    }
                ],
                
            });
            const commentJson = fullComment.toJSON();
             const userLike = await sequelize.models.LikeComment.findOne({
                    where: {userId: userId, commentId: commentJson.commentId}
                    });
            commentJson.isUserLike = (userLike)? true : false;
            commentData.likeNum = 0;
            // Convert time to UTC+7 instead of UTC
            commentJson.time = moment(commentJson.time).tz('Asia/Ho_Chi_Minh').format(); // e.g., 2025-06-26T15:24:45+07:00
            return getCommentImageData(commentJson);
        } catch (err) {
            if (err instanceof ApiError) throw err;
            throw new ApiError(`Lỗi khi tạo comment: ${err.message}`, 500);
        }
    },
     async unlikeComment(commentId,userId) {
        try {
            const commentData = await sequelize.models.Comment.findByPk(commentId);
            if (!commentData) {
                throw new ApiError('comment không tồn tại', 404);
            }
           const like_comment = await sequelize.models.LikeComment.findOne({
                where: { commentId: commentId, userId: userId 

                }});
            if (like_comment) {
                await like_comment.destroy();
            }
            return { message: 'unlike comment thành công', commentId };
        } catch (err) {
            if (err instanceof ApiError) throw err;
            throw new ApiError(`Lỗi khi unlike comment: ${err.message}`, 500);
        }
     },
     async likeComment(commentId,userId) {
        try {
            const commentData = await sequelize.models.Comment.findByPk(commentId);
            if (!commentData) {
                throw new ApiError('comment không tồn tại', 404);
            }
           const like_comment = await sequelize.models.LikeComment.create({commentId: commentId, userId: userId });
            return { message: 'like comment thành công', commentId };
        } catch (err) {
            if (err instanceof ApiError) throw err;
            throw new ApiError(`Lỗi khi like comment: ${err.message}`, 500);
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
            const likeComments = await sequelize.models.LikeComment.findAll({
                where: { commentId: commentId }
            });
            // Xoá tất cả like liên quan đến comment này
            for (const likeComment of likeComments) {
                await likeComment.destroy();
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