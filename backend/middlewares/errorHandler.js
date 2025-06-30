const errorHandler = (err, req, res, next) => {
  console.error(err.stack)

  // Mặc định mã trạng thái và thông điệp
  let statusCode = err.statusCode || 500
  let message = err.message || 'Lỗi máy chủ nội bộ.'

  // Xử lý lỗi Sequelize (nếu có)
  if (err.name === 'SequelizeValidationError') {
    statusCode = 400
    message = err.errors.map((e) => e.message).join(', ')
  } else if (err.name === 'SequelizeUniqueConstraintError') {
    statusCode = 400
    message = 'Dữ liệu đã tồn tại, không thể tạo trùng.'
  }

  const response = {
    status: statusCode,
    message,
  }

  res.status(statusCode).json(response)
}

export default errorHandler
