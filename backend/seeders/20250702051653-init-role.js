export async function up(queryInterface, Sequelize) {
  await queryInterface.bulkInsert('role', [
    {
      roleId: 1,
      roleName: 'admin',
    },
    {
      roleId: 2,
      roleName: 'user',
    },
  ]);
}

export async function down(queryInterface, Sequelize) {
  await queryInterface.bulkDelete('role', {
    roleName: { [Sequelize.Op.in]: ['user', 'admin'] },
  });
}
