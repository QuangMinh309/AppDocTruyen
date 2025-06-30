import CommentService from '../../services/comment.service.js'
import ApiError from '../../utils/api_error.util.js';




// Broadcast đến tất cả client trong chapter
const broadcastToClients = (ws, action, data, clients) => {

    clients.forEach((clientWs, userId) => {
        if (ws.userId === userId) return; // Không gửi lại cho chính mình
        if (!clientWs || clientWs.readyState !== clientWs.OPEN) return; // Kiểm tra kết nối
        if (!clientWs.userId) return; // Kiểm tra userId
        if (clientWs.chapterId !== ws.chapterId) return; // Chỉ gửi đến những client trong cùng chapter
        clientWs.send(JSON.stringify({
            action: `BRC_${action}`,
            payload: [data]
        }));
    });
}



async function init(ws, request, clients) {
    try {
        // Lấy URL đầy đủ ví dụ: ws://localhost/chat?chapter=1233&token=abc123
        // Ở đây request.url là: /chat?chapter=1233&token=abc123
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
        ws.send(JSON.stringify({ error: 'Invalid JSON' }));
        return;
    }


    const { action, payload } = data;

    try {
        switch (action) {
            case 'CREATE_COMMENT':
                if (!payload) {
                    ws.send(JSON.stringify({ error: 'Nội dụng không được để trống!' }));
                    return;
                }
                const newComment = await CommentService.createComment({ ...payload, userId: ws.userId });
                console.log('New comment created:', newComment);
                ws.send(JSON.stringify({ success: true, action, comment: newComment }));
                broadcastToClients(ws, action, newComment, clients);
                break;

            case 'UPDATE_COMMENT':
                if (!payload?.commentId) {
                    ws.send(JSON.stringify({ error: 'commentId không được để trống!' }));
                    return;
                }
                const updatedComment = await CommentService.updateComment(payload.commentId, payload);
                if (!updatedComment) {
                    ws.send(JSON.stringify({ error: 'comment not found' }));
                    return;
                }
                ws.send(JSON.stringify({ success: true, action, comment: updatedComment }));
                broadcastToClients(ws, action, updatedComment, clients);
                break;

            case 'DELETE_COMMENT':
                if (!payload?.commentId) {
                    ws.send(JSON.stringify({ error: 'commentId không được để trống!' }));
                    return;
                }
                const deleted = await CommentService.deleteComment(payload.commentId);
                if (!deleted) {
                    ws.send(JSON.stringify({ error: 'Note not found' }));
                    return;
                }
                ws.send(JSON.stringify({ success: true, action, comment: deleted }));
                broadcastToClients(ws, action, deleted, clients);
                break;

            case 'FETCH_COMMENT_BY_CHAPTER':
                if (!payload?.chapterId) {
                    ws.send(JSON.stringify({ error: 'chapterId không được để trống!' }));
                    return;
                }
                const allComments = await CommentService.getAllCommentsOfChapter(payload.chapterId);
                ws.send(JSON.stringify({ success: true, action: action, comments: allComments }));

                break;

            case 'LIKE_COMMENT':
                if (!payload?.commentId) {
                    ws.send(JSON.stringify({ error: 'commentId không được để trống!' }));
                    return;
                }
                const likedComment = await CommentService.likeComment(ws.userId, payload.commentId);
                if (!likedComment) {
                    ws.send(JSON.stringify({ error: 'Không thể thích bình luận này' }));
                    return;
                }
                ws.send(JSON.stringify({ success: true, action, comment: likedComment }));
                broadcastToClients(ws, action, likedComment, clients);
                break;
            case 'UNLIKE_COMMENT':
                if (!payload?.commentId) {
                    ws.send(JSON.stringify({ error: 'commentId không được để trống!' }));
                    return;
                }
                const unlikedComment = await CommentService.unlikeComment(ws.userId, payload.commentId);
                if (!unlikedComment) {
                    ws.send(JSON.stringify({ error: 'Không thể bỏ thích bình luận này' }));
                    return;
                }
                ws.send(JSON.stringify({ success: true, action, comment: unlikedComment }));
                broadcastToClients(ws, action, unlikedComment, clients);
                break;
            default:
                ws.send(JSON.stringify({ error: 'Unknown action' }));
                break;
        }
    } catch (err) {
        if (err instanceof ApiError) throw err;
        throw new ApiError(`Lỗi thao tác tin nhắn: ${err.message}`, 500);

    }
}
export default { init, onMessage };