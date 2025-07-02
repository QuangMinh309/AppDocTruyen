import ApiError from '../../utils/api_error.util.js';



async function init(ws, request, clients) {
    try {
        console.log(`User ${ws.userId} connected.`);
        ws.send(JSON.stringify({ type: 'CONNECTION_SUCCESS', message: 'kết nối thành công tới /ws/notification!' }));
    } catch (error) {
        if (error instanceof ApiError) throw error;
        throw new ApiError(`Lỗi khởi tạo kết nối: ${error.message}`, 500);
    }
}

// Hàm xử lý tin nhắn từ client
async function onMessage(ws, message, clients) {
    
}
export default { init, onMessage };