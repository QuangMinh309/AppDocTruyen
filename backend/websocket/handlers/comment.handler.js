// comment.handler.js
import CommentService from '../../services/comment.service.js';
import ApiError from '../../utils/api_error.util.js';
import {
    createCommentSchema,
    updateCommentSchema,
    deleteCommentSchema,
    fetchCommentByChapterSchema,
    validateMessage,
} from '../../validators/comment.validation.js';

// Broadcast đến tất cả client trong chapter
const broadcastToClients = (ws, action, data, clients) => {
    clients.forEach((client) => {
        const clientWs = client?.get('/ws/comment');
        if (!clientWs || clientWs.readyState !== clientWs.OPEN) return; // Kiểm tra kết nối

        if (clientWs.chapterId !== ws.chapterId) return; // Chỉ gửi đến những client trong cùng chapter



        clientWs.send(JSON.stringify({
            action: `BRC_${action}`,
            payload: data,
        }));
    });
};

async function init(ws, request, clients) {
    try {
        // Lấy URL đầy đủ ví dụ: ws://localhost/comment?chapter=1233&token=abc123   
        const fullUrl = `ws://${request.headers.host}${request.url}`;
        const myURL = new URL(fullUrl);

        // Lấy chapterId từ query
        const chapterId = myURL.searchParams.get('chapter');

        if (!chapterId) {
            throw new ApiError('chapterId không được để trống!', 400);
        }

        ws.chapterId = chapterId;

        console.log(`User ${ws.userId} connected.`);
        ws.send(JSON.stringify({ type: 'CONNECTION_SUCCESS', message: 'kết nối thành công tới /ws/comment!' }));
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
            case 'CREATE_COMMENT':
                validateMessage(createCommentSchema, data); // Validate trước khi xử lý
                const newComment = await CommentService.createComment({ ...payload, userId: ws.userId, chapterId: ws.chapterId }, ws.userId);
                // console.log(' comment data:', newComment);
                ws.send(JSON.stringify({ success: true, action, payload: newComment }));
                broadcastToClients(ws, action, newComment, clients);
                break;

            case 'UPDATE_COMMENT':
                validateMessage(updateCommentSchema, data); // Validate trước khi xử lý
                const updatedComment = await CommentService.updateComment(payload.commentId, payload, ws.userId);
                if (!updatedComment) {
                    throw new ApiError('không tìm thấy comment để cập nhật!', 404);
                }
                ws.send(JSON.stringify({ success: true, action, payload: updatedComment }));
                broadcastToClients(ws, action, updatedComment, clients);
                break;

            case 'DELETE_COMMENT':
                validateMessage(deleteCommentSchema, data); // Validate trước khi xử lý
                const deleted = await CommentService.deleteComment(payload.commentId);
                if (!deleted) {
                    throw new ApiError('không tìm thấy comment để xóa!', 404);
                }
                ws.send(JSON.stringify({ success: true, action, payload: deleted }));
                broadcastToClients(ws, action, deleted, clients);
                break;

            case 'FETCH_COMMENT_BY_CHAPTER':
                validateMessage(fetchCommentByChapterSchema, data); // Validate trước khi xử lý
                const allComments = await CommentService.getAllCommentsOfChapter(ws.chapterId,ws.userId);
                console.log(allComments)
                ws.send(JSON.stringify({ success: true, action: action, payload: allComments }));
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