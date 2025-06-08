// middlewares/webSocketErrorHandler.js
const errorHandler = (ws, error) => {
    console.error('WebSocket error:', error.message);
    ws.send(JSON.stringify({
        type: 'ERROR',
        status: error.status || 500,
        message: error.message || 'Lỗi server nội bộ'
    }));
};
export default errorHandler;