// server.js
import express from "express";
import cors from "cors";
import dotenv from "dotenv";
import db from "./models/index.js";
import imageRoutes from "./routes/imageRoutes.js";

dotenv.config(); // Load .env before anything else

const app = express();
const sequelize = db.sequelize;

// Middleware
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(express.static("public"));
app.use("/uploads", express.static("uploads"));

// Routes
app.use("/api/images", imageRoutes);

app.get("/", (req, res) => {
  res.send("Hello from backend!");
});

// Start server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server running at http://localhost:${PORT}`);
});

// DB connection
sequelize
  .authenticate()
  .then(() => {
    console.log("Đã kết nối thành công đến database!");
  })
  .catch((err) => {
    console.error("Lỗi kết nối database:", err);
  });
