# Backend cho AppStory

## Cấu trúc thư mục
backend/
├── config/                # Cấu hình (DB, môi trường)
│   └── db.js
├── controllers/
├── middleware/
├── models/
├── routes/
├── migrations/            # <--- Nơi chứa các file migration (nếu có)
│   └── 20250414-create-users.js
├── seeders/               # (Tuỳ chọn) Tạo dữ liệu mẫu
├── .env
├── .gitignore
├── server.js
└── package.json

## Hướng dẫn dùng migration
- Khởi tạo: npx sequelize-cli init
- Tạo một migration: npx sequelize-cli migration:generate --name create-users (tạo file create-users như trên)
- Chạy migration: npx sequelize-cli db:migrate (db sẽ tạo bảng)