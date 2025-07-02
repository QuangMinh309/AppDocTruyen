export async function up(queryInterface, Sequelize) {
  await queryInterface.bulkInsert('role', [
    {
      roleId: 1,
      roleName: 'user',
    },
    {
      roleId: 2,
      roleName: 'admin',
    },
  ]);
}

export async function down(queryInterface, Sequelize) {
  await queryInterface.bulkDelete('role', {
    roleName: { [Sequelize.Op.in]: ['user', 'admin'] },
  });
}
