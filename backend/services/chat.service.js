import { sequelize } from '../models/index.js';
import ApiError from '../utils/api_error.util.js';
import { getImageUrlFromCloudinary, uploadBase64ToCloudinary, deleteImageOnCloudinary } from './cloudinary.service.js';
import moment from 'moment-timezone'; // Use moment-timezone for timezone support
import CommunityService from './community.service.js';

const getChatImageData = async (chatJson) => {
    try {
        if (chatJson.sender) {
            chatJson.sender.avatarUrl = await getImageUrlFromCloudinary(chatJson.sender.avatarId);
            delete chatJson.sender.avatarId;
        }
    } catch (cloudinaryErr) {
        chatJson.sender.avatarUrl = "";
        console.error(`Lỗi khi lấy dữ liệu hình ảnh từ Cloudinary: ${cloudinaryErr.message}`);
    }

    try {
        chatJson.commentPicUrl = await getImageUrlFromCloudinary(chatJson.commentPicId);
        delete chatJson.commentPicId;
    } catch (cloudinaryErr) {
        chatJson.commentPicUrl = null;
        console.error(`Lỗi khi lấy dữ liệu hình ảnh từ Cloudinary: ${cloudinaryErr.message}`);

    }
    return chatJson;
}

const ChatService = {

    async getAllChatsOfCommunity(id, userId) {
        try {
            const joinChat = CommunityService.addMember(id, userId)
            if (!joinChat) throw new ApiError(`Bạn chưa thể tham gia cộng đồng này!`, 500);
            const chats = await sequelize.models.Chat.findAll({
                limit:50,
                where: { communityId: id },
                include: [

                    { model: sequelize.models.User, as: 'sender', attributes: ['userId', 'dUserName', 'avatarId'] },
                ],
            });
            const chatPromises = chats.map(chat => {
                const chatJson = chat.toJSON();
                chatJson.isUser = (chatJson.sender.userId == userId) ? true : false

                // Convert time to UTC+7 instead of UTC
                chatJson.time = moment(chatJson.time).tz('Asia/Ho_Chi_Minh').format(); // e.g., 2025-06-26T15:24:45+07:00

                return getChatImageData(chatJson);
            });
            return await Promise.all(chatPromises);
        } catch (err) {
            if (err instanceof ApiError) throw err;
            throw new ApiError(`Lỗi khi lấy danh sách cuộc trò chuyện: ${err.message}`, 500);
        }
    },

    async createChat(data, userId) {
        try {
            if (!data.content && !data.commentPicData) {
                throw new ApiError('Không được để trống nội dung!', 400);
            }
            let publicId = null;
            let commentPicUrl = null;
            if (data.commentPicData) {
                const result = await uploadBase64ToCloudinary(data.commentPicData, "user/community/chat");
                publicId = result.public_id;
                commentPicUrl = result.secure_url;

            }
            data.time = new Date()
            const chatData = await sequelize.models.Chat.create({ ...data, commentPicId: publicId });
            const fullChat = await sequelize.models.Chat.findByPk(chatData.chatId, {
                include: [

                    { model: sequelize.models.User, as: 'sender', attributes: ['userId', 'dUserName', 'avatarId'] },
                ],
            });
            const chatJson = fullChat.toJSON();
            chatJson.isUser = (chatJson.sender.userId == userId) ? true : false
            // Convert time to UTC+7 instead of UTC
            chatJson.time = moment(chatJson.time).tz('Asia/Ho_Chi_Minh').format(); // e.g., 2025-06-26T15:24:45+07:00
            // console.log('Full chat data:', chatJson);
            return getChatImageData(chatJson);
        } catch (err) {
            if (err instanceof ApiError) throw err;
            throw new ApiError(`Lỗi khi tạo cuộc trò chuyện: ${err.message}`, 500);
        }
    },

    async updateChat(chatId, data, userId) {
        try {
            if (!data.content && !data.commentPicData) {
                throw new ApiError('Không có nội dng cần cập nhật!', 400);
            }
            const chatData = await sequelize.models.Chat.findByPk(chatId);
            if (!chatData) {
                throw new ApiError('Cuộc trò chuyện không tồn tại', 404);
            }
            let publicId = null;
            let commentPicUrl = null;
            if (data.commentPicData) {
                const result = await uploadBase64ToCloudinary(data.commentPicData, "user/community/chat");
                publicId = result.public_id;
                commentPicUrl = result.secure_url;

            }
            await chatData.update({ ...data, commentPicId: publicId });
            const updatedChat = await sequelize.models.Chat.findByPk(chatId, {
                include: [
                    { model: sequelize.models.User, as: 'sender', attributes: ['userId', 'dUserName', 'avatarId'] },
                ],
            });

            const chatJson = fullChat.toJSON();
            chatJson.isUser = (chatJson.sender.userId == userId) ? true : false
            // Convert time to UTC+7 instead of UTC
            chatJson.time = moment(chatJson.time).tz('Asia/Ho_Chi_Minh').format(); // e.g., 2025-06-26T15:24:45+07:00
            // console.log('Full chat data:', chatJson);
            return getChatImageData(chatJson);
        } catch (err) {
            if (err instanceof ApiError) throw err;
            throw new ApiError(`Lỗi khi cập nhật cuộc trò chuyện: ${err.message}`, 500);
        }
    },

    async deleteChat(chatId) {
        try {
            const chatData = await sequelize.models.Chat.findByPk(chatId);
            if (!chatData) {
                throw new ApiError('Cuộc trò chuyện không tồn tại', 404);
            }
            // Xoá hình ảnh nếu có
            if (chatData.commentPicId) {
                try {
                    await deleteImageOnCloudinary(chatData.commentPicId);
                } catch (cloudinaryErr) {
                    console.error(`Lỗi khi xoá hình ảnh từ Cloudinary: ${cloudinaryErr.message}`);
                    throw new ApiError(`Lỗi khi xoá hình ảnh: ${cloudinaryErr.message}`, 500);
                }
            }
            await chatData.destroy();
            return { message: 'Xoá cuộc trò chuyện thành công', chatId };
        } catch (err) {
            if (err instanceof ApiError) throw err;
            throw new ApiError(`Lỗi khi xoá cuộc trò chuyện: ${err.message}`, 500);
        }
    },
};

export default ChatService;