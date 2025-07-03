// chat.handler.js
import ChatService from '../../services/chat.service.js';
import ApiError from '../../utils/api_error.util.js';
import {
    createChatSchema,
    updateChatSchema,
    deleteChatSchema,
    fetchChatByCommunitySchema,
    validateMessage,
} from '../../validators/chat.validation.js';

// Broadcast đến tất cả client trong community
const broadcastToClients = (ws, action, data, clients) => {
    clients.forEach((client) => {
        const clientWs = client?.get('/ws/chat');
        if (!clientWs || clientWs.readyState !== clientWs.OPEN) return; // Kiểm tra kết nối
        
        if (clientWs.communityId !== ws.communityId) return; // Chỉ gửi đến những client trong cùng community
 
        const dataToSend = { ...data };
        dataToSend.isUser = data.sender?.userId === clientWs.userId;
console.log('broadcastToClients', dataToSend)
        clientWs.send(JSON.stringify({
            action: `BRC_${action}`,
            payload: dataToSend,
        }));
    });
};

async function init(ws, request, clients) {
    try {
        // Lấy URL đầy đủ ví dụ: ws://localhost/chat?community=1233&token=abc123
        const fullUrl = `ws://${request.headers.host}${request.url}`;
        const myURL = new URL(fullUrl);

        // Lấy communityId từ query
        const communityId = myURL.searchParams.get('community');


        if (!communityId) {
            throw new ApiError('communityId không được để trống!', 400);
        }

        ws.communityId = communityId;

        console.log(`User ${ws.userId} connected.`);
        ws.send(JSON.stringify({ type: 'CONNECTION_SUCCESS', message: 'kết nối thành công tới /ws/chat!' }));
        console.log(`send connected message.`);
    } catch (error) {
        if (error instanceof ApiError) throw error;
        throw new ApiError(`Lỗi khởi tạo kết nối: ${error.message}`, 500);
    }
}

// Hàm xử lý tin nhắn từ client
async function onMessage(ws, message, clients) {
    let data;
    try {
        data = JSON.parse(message);
    } catch {
        throw new ApiError('dữ liệu không hợp lệ!', 400);
    }

    const { action, payload } = data;

    try {

        switch (action) {
            case 'CREATE_CHAT':
                validateMessage(createChatSchema, data); // Validate trước khi xử lý
                const newChat = await ChatService.createChat({ ...payload, senderId: ws.userId, communityId: ws.communityId }, ws.userId);
               
                ws.send(JSON.stringify({ success: true, action, payload: newChat }));
                broadcastToClients(ws, action, newChat, clients);
                break;

            case 'UPDATE_CHAT':
                validateMessage(updateChatSchema, data); // Validate trước khi xử lý
                const updatedChat = await ChatService.updateChat(payload.chatId, payload, ws.userId);
                if (!updatedChat) {
                    throw new ApiError('không tìm thấy chat để cập nhật!', 404);
                }
                ws.send(JSON.stringify({ success: true, action, payload: updatedChat }));
                broadcastToClients(ws, action, updatedChat, clients);
                break;

            case 'DELETE_CHAT':
                validateMessage(deleteChatSchema, data); // Validate trước khi xử lý
                const deleted = await ChatService.deleteChat(payload.chatId);
                if (!deleted) {
                    throw new ApiError('không tìm thấy chat để xóa!', 404);
                }
                ws.send(JSON.stringify({ success: true, action, payload: deleted }));
                broadcastToClients(ws, action, deleted, clients);
                break;

            case 'FETCH_CHAT_BY_COMMUNITY':
                validateMessage(fetchChatByCommunitySchema, data); // Validate trước khi xử lý
            
                const allChats = await ChatService.getAllChatsOfCommunity(ws.communityId, ws.userId);
                // console.log(allChats)
                ws.send(JSON.stringify({ success: true, action: action, payload: allChats }));
                break;

            default:
                throw new ApiError(`Không hỗ trợ hành động: ${action}`, 400);
        }
    } catch (err) {
        if (err instanceof ApiError) throw err;
        throw new ApiError(`Lỗi khi xử lý tin nhắn: ${err.message}`, 500);
    }
}

export default { init, onMessage };