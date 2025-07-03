# Ứng dụng Đọc Truyện Trực Tuyến

Ứng dụng cho phép người dùng đọc truyện trực tuyến, theo dõi, đánh dấu truyện yêu thích, tương tác bằng bình luận và quản lý nội dung truyện cho người sáng tác.

---

## Giới thiệu

Dự án được xây dựng nhằm cung cấp nền tảng đọc truyện tiện lợi, hỗ trợ phân quyền người dùng (đọc/viết), tối ưu trải nghiệm người dùng và dễ dàng mở rộng.

---

## Công cụ & Thư viện sử dụng

### Backend (Node.js + Express + Sequelize):
- **Express.js**: Framework xây dựng API RESTful
- **Sequelize ORM**: Quản lý truy vấn CSDL MySQL
- **JWT**: Xác thực người dùng bằng token
- **Bcrypt**: Mã hóa mật khẩu
- **Cloudinary SDK**: Lưu trữ ảnh trên cloud
- **Joi**: Xác thực dữ liệu đầu vào
- **WebSocket (ws)**: Hỗ trợ chat/bình luận thời gian thực

### Mobile:
- **Kotlin + Jetpack Compose**: Giao diện mobile
- **MVVM + Clean Architecture**: Tổ chức code dễ bảo trì

---

## Cài đặt môi trường

```
# Clone project
git clone https://github.com/QuangMinh309/AppDocTruyen.git
cd app-doc-truyen

# Cài đặt backend
cd backend
npm install
cp .env.example .env
npm run dev
```

---

## Chức năng chính

Người dùng:
- Đăng ký / Đăng nhập / Phân quyền (user & author & admin)
- Theo dõi truyện, đánh dấu chương đã đọc
- Đọc truyện, xem chương, nhận thông báo chương mới
- Bình luận chương (chat thời gian thực)

Tác giả:
- Tạo / chỉnh sửa / xóa truyện
- Quản lý chương, hình ảnh minh họa
- Nhận tiền nếu tạo truyện tính phí và có người mua

Quản trị viên:
- Quản lý tài khoản
- Quản lý giao dịch
- Quản lý người dùng
- Xem báo cáo thống kê

---

## Bảo mật và hiệu suất

- Mã hóa mật khẩu bằng Bcrypt
- Xác thực và phân quyền bằng JWT
- Sử dụng Joi để kiểm tra dữ liệu đầu vào
- Chống spam qua rate-limiting và kiểm tra token
- WebSocket tối ưu để chat thời gian thực theo community
- Ảnh được lưu trên Cloudinary – giảm tải server

---

## Tài liệu

- [Express.js Documentation](https://expressjs.com/)
- [Sequelize ORM Documentation](https://sequelize.org/)
- [JWT Introduction](https://jwt.io/introduction)
- [Bcrypt Documentation](https://www.npmjs.com/package/bcrypt)
- [Cloudinary Documentation](https://cloudinary.com/documentation)
- [Joi Validation](https://joi.dev/api/)
- [WebSocket (ws) Library](https://github.com/websockets/ws)
- [React Documentation](https://react.dev/)
- [TailwindCSS Documentation](https://tailwindcss.com/docs)
- [Axios Documentation](https://axios-http.com/docs/intro)
- [React Router](https://reactrouter.com/en/main)
- [Redux Toolkit](https://redux-toolkit.js.org/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Jetpack Compose](https://developer.android.com/jetpack/compose/documentation)
- [MVVM Pattern](https://developer.android.com/jetpack/guide#recommended-app-arch)
- [Clean Architecture](https://developer.android.com/jetpack/guide#recommended-app-arch)

---

## Thành viên thực hiện
- **Trần Thị Thu Hoài**
- **Phạm Ngọc Quang Minh**
- **Tăng Minh Hoàng**
- **Nguyễn Minh Thiện**