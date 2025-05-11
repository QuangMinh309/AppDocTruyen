/** @type {import('sequelize-cli').Migration} */
export default {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable("name_list", {
      nameListId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER,
      },
      nameList: {
        type: Sequelize.STRING(255),
      },
      userId: {
        type: Sequelize.INTEGER,
        references: {
          model: "user",
          key: "userId",
        },
      },
      description: {
        type: Sequelize.STRING(1500),
      },
    });
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable("name_list");
  },
};
