import dotenv from 'dotenv'
dotenv.config({ path: './backend/.env' });

import express from 'express'
import cors from 'cors'   
import db from './models/index.js'; // Import object db
import imageRoutes from './routes/imageRoutes.js'// Import route xử lý ảnh

const sequelize = db.sequelize; // Lấy sequelize instance từ db
const app = express()

// Middleware
app.use(cors()); // Cho phép kết nối từ Android app
app.use(express.json());
app.use(express.urlencoded({ extended: true })); // Cho phép gửi dữ liệu từ form lên server
app.use(express.static('public')); // Cho phép truy cập vào thư mục public
app.use('/uploads', express.static('uploads'));  // Dùng để phục vụ file tạm thời

// Routes
app.use('/api/images', imageRoutes);  // URL gốc sẽ là /api/images/:imageId (lấy ảnh) và /api/images/upload (upload ảnh)

//run server
const PORT = process.env.PORT || 3000;

app.get("/", (req, res) => {
  res.send("I'm alive!");
});
  
app.listen(PORT , () => {
  console.log(`Server running at http://localhost:${PORT}`);
});


// Gọi kết nối DB
sequelize.authenticate()
  .then(() => {
    console.log('Đã kết nối thành công đến database!');
  })
  .catch((err) => {
    console.error('Lỗi kết nối DB:', err);
  });

app.get('/', (req, res) => {
  res.send('Hello from backend!');
});


