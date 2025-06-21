export async function up(queryInterface, Sequelize) {
  await queryInterface.createTable('parameter', {
    id: {
      type: Sequelize.INTEGER,
      primaryKey: true,
      autoIncrement: true,
      allowNull: false,
    },
    Chapter_Access_Duration: {
      type: Sequelize.INTEGER,
      allowNull: false,
      comment: 'Thời hạn truy cập chương (tính bằng ngày)',
    },
    Story_Access_Duration: {
      type: Sequelize.INTEGER,
      allowNull: false,
      comment: 'Thời hạn truy cập truyện (tính bằng ngày)',
    },
    Max_MemberNum: {
      type: Sequelize.INTEGER,
      allowNull: false,
      comment: 'Số lượng thành viên tối đa',
    },
    OTP_Valid_Period: {
      type: Sequelize.INTEGER,
      allowNull: false,
      comment: 'Thời hạn hiệu lực của OTP (tính bằng phút)',
    },
    Premium_Period: {
      type: Sequelize.INTEGER,
      allowNull: false,
      comment: 'Thời hạn premium (tính bằng ngày)',
    },
  })
}

export async function down(queryInterface, Sequelize) {
  await queryInterface.dropTable('parameter')
}
