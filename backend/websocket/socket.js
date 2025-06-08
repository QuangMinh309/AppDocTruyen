import chatHandler from './handlers/chat.handler.js';
import errorHandler from '../middlewares/webSocketErrorHandler.js'
import commentHandler from './handlers/comment.handler.js';
import { authenticateWebSocket } from '../middlewares/auth.middleware.js'
import ApiError from '../utils/api_error.util.js';

const routerHandlers = {
    '/ws/chat': chatHandler,
    '/ws/comment': commentHandler,
};

// Map lưu trữ client: userId -> ws
const clients = new Map();


const onConnection = async (ws, request) => {

    try {
        // Lấy URL đầy đủ ví dụ: ws://localhost/chat?community=1233&token=abc123
        const fullUrl = `ws://${request.headers.host}${request.url}`;
        const myURL = new URL(fullUrl);

        // Lấy path
        const pathname = myURL.pathname;  // ví dụ: "/chat"

        // Xác thực dựa vào token
        const user = await authenticateWebSocket(ws, request);
        if (!user) {
            throw new ApiError('lỗi xác thực người dùng!', 401);
        }

        ws.userId = user.userId;

        // Kiểm tra xem userId đã có trong clients chưa
        // Nếu có thì đóng kết nối cũ
        if (clients.has(ws.userId)) {
            const oldWs = clients.get(ws.userId);
            oldWs.close(1000, 'Thiết lập kết nối mới');
        }

        clients.set(ws.userId, ws);

        // Lấy handler theo path

        const handler = routerHandlers[pathname];
        if (!handler) {
            throw new ApiError(`Không tìm thấy handler cho path: ${pathname}`, 404);
        }

        handler.init(ws, request, clients);

        ws.on('message', async (message) => {
            try {
                await handler.onMessage(ws, message, clients);
            } catch (error) {
                errorHandler(ws, error);
            }
        });

        // Xử lý ngắt kết nối
        ws.on('close', () => {
            clients.delete(ws.userId);
            console.log(`User ${ws.userId} disconnected.`);
        });
    } catch (error) {
        errorHandler(ws, error);
        ws.close();
    }
}

export default function (wss) {
    wss.on('connection', onConnection);
};
