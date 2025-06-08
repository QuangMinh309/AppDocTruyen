import bcrypt from 'bcrypt'

export async function up(queryInterface, Sequelize) {
  const passwordHash = await bcrypt.hash('admin123', 10)

  return queryInterface.bulkInsert('user', [
    {
      userName: 'admin',
      dUserName: 'Admin',
      roleId: 1,
      mail: 'admin@example.com',
      about: 'Administrator account',
      DOB: new Date('1990-01-01'),
      followerNum: 0,
      password: passwordHash,
      avatarId: null,
      backgroundId: null,
      wallet: 0.0,
      isPremium: true,
      status: 'active',
    },
  ])
}

export async function down(queryInterface, Sequelize) {
  return queryInterface.bulkDelete('user', { userName: 'admin' }, {})
}