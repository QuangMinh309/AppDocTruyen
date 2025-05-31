export async function up(queryInterface, Sequelize) {
  await queryInterface.bulkInsert('parameter', [{
    Chapter_Access_Duration: 7,
    Story_Access_Duration: 30,
    Max_MemberNum: 100,
    OTP_Valid_Period: 5,
    Premium_Period: 30
  }])
}

export async function down(queryInterface, Sequelize) {
  await queryInterface.bulkDelete('parameter', null, {})
}
