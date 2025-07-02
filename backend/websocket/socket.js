import chatHandler from './handlers/chat.handler.js';
import errorHandler from '../middlewares/webSocketErrorHandler.js'
import commentHandler from './handlers/comment.handler.js';
import { authenticateWebSocket } from '../middlewares/auth.middleware.js'
import ApiError from '../utils/api_error.util.js';
import notificationHandler from './handlers/notification.handler.js';

const routerHandlers = {
    '/ws/chat': chatHandler,
    '/ws/comment': commentHandler,
    '/ws/notification':notificationHandler
};

// Map lưu trữ client: userId -> ws
export const clients = new Map();


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
        if (!clients.has(ws.userId)) {
            clients.set(ws.userId, new Map());
        }

        const userSockets = clients.get(ws.userId);
        // Kiểm tra nếu `ws` đã tồn tại trong tập socket
        if (userSockets && userSockets.has(pathname)) {
            const oldWs = userSockets.get(pathname);
            oldWs.close(1000, 'Thiết lập kết nối mới');
        }
        clients.get(ws.userId).set(pathname,ws);

        // Lấy handler theo path

        const handler = routerHandlers[pathname];
        if (!handler) {
            throw new ApiError(`Không tìm thấy handler cho path: ${pathname}`, 404);
        }

        handler.init(ws, request, clients);


        ws.on('message', async (message) => {
            try {
                console.log("onmessage call!")
                await handler.onMessage(ws, message, clients);
            } catch (error) {
                errorHandler(ws, error);
            }
        });

        // Xử lý ngắt kết nối
        ws.on('close', () => {
            const oldWs = userSockets.get(pathname);
            if (oldWs) {
                oldWs.close(1000, 'Thiết lập kết nối mới');
            }
            console.log(`User ${ws.userId} disconnected with room ${pathname}.`);
        });
    } catch (error) {
        errorHandler(ws, error);
        ws.close();
    }
}

export default function (wss) {
    wss.on('connection', onConnection);
};
