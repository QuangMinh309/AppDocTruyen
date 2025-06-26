// server.js
import { WebSocketServer } from 'ws'
import express from 'express'
import http from 'http'
import cors from 'cors'
import dotenv from 'dotenv'
import db from './models/index.js'
import errorHandler from './middlewares/errorHandler.js'

import route from './routes/index.js'
import WebSocket from './websocket/socket.js'

dotenv.config()

// Khởi tạo server Express và WebSocket
const app = express()
const server = http.createServer(app)
const wss = new WebSocketServer({ server })

const sequelize = db.sequelize

// Middleware
app.use(cors())
app.use(express.json())
app.use(express.urlencoded({ extended: true }))
app.use(express.static('public'))

route(app)

app.get('/', (req, res) => {
  res.send('Hello from backend!')
})

// Error handling middleware
app.use(errorHandler)
process.on('unhandledRejection', (reason, promise) => {
  console.error('Unhandled Rejection at:', promise, 'reason:', reason);
  // Có thể gửi thông báo hoặc log thêm
});

// // WebSocket connection handling
// // Store connected clients  :userId => ws
// const clients = new Map()

// // WebSocket logic với middleware
// wss.on('connection', async (ws, req) => {
//   const user = await authenticateWebSocket(ws, req);
//   if (user) {
//     ws.userId = user.userId;
//     clients.set(ws.userId, ws);
//     console.log(`User ${user.userId} connected.`);

//     ws.on('message', (message) => {
//       console.log(`Received: ${message} from ${user.userId}`);
//     });

//     ws.on('close', () => {
//       clients.delete(user.userId);
//       console.log(`User ${user.userId} disconnected.`);
//     });
//   }
// });
// Start server

WebSocket(wss)

const PORT = process.env.DB_PORT || 3000
server.listen(PORT, '0.0.0.0', () => {
  console.log(`Server running at http://localhost:${PORT}`)
})

// DB connection
sequelize
  .authenticate()
  .then(() => {
    console.log('Đã kết nối thành công đến database!')
  })
  .catch((err) => {
    console.error('Lỗi kết nối database:', err)
  })
