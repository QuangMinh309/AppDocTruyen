/** @type {import('sequelize-cli').Migration} */
export default {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable("community", {
      communityId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER,
      },
      communitytName: {
        type: Sequelize.STRING,
      },
      categoryId: {
        type: Sequelize.INTEGER,
        references: {
          model: "category",
          key: "categoryId",
        },
      },
      avatarId: {
        type: Sequelize.STRING,
      },
      menberNum: {
        type: Sequelize.INTEGER,
      },
      description: {
        type: Sequelize.STRING(1500),
      },
    });
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable("community");
  },
};
