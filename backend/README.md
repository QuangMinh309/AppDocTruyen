# Backend cho AppStory

## Cấu trúc thư mục

```
backend/
|-- config/            # cấu hình DB, biến môi trường
|-- controllers/       # xử lý logic request/response
|-- middlewares/       # middleware chung
|-- migrations/        # file migration DB
|-- models/            # định nghĩa model Sequelize
|-- routes/            # định nghĩa route API
|-- seeders/           # file seed dữ liệu mẫu
|-- services/          # chứa nghiệp vụ + xử lý logic phức tạp
|-- server.js          # file khởi chạy app
|-- package.json       # cấu hình dự án + script
```

## Hướng dẫn tạo migration

- Khởi tạo: npx sequelize-cli init
- Tạo file migration: npx sequelize-cli migration:generate --name <filename>
- Tạo file seed: npx sequelize-cli seed:generate --name <filename>

## Hướng dẫn chạy migration

- npm run migrate:entities: chạy migration thư mục entities
- npm run migrate:relations: chạy migration thư mục relations
- npm run migrate:undo:entities: undo migration thư mục entities
- npm run migrate:undo:relations: undo migration thư mục relations
- npm run seed: chạy seed dữ liệu mẫu
- npm run seed:undo: xóa seed dữ liệu mẫu
- npm run db:reset:entities: reset DB (entities)
- npm run db:reset:relations: reset DB (relations)
- npm start: chạy server

## Hướng dẫn dùng

- cd backend
- npm install
- npm start
