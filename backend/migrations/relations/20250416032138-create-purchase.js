/** @type {import('sequelize-cli').Migration} */
export default {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable("purchase", {
      purchasedId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER,
      },
      userId: {
        type: Sequelize.INTEGER,
        references: {
          model: "user",
          key: "userId",
        },
      },
      storyId: {
        type: Sequelize.INTEGER,
        references: {
          model: "story",
          key: "storyId",
        },
      },
      chapterId: {
        type: Sequelize.INTEGER,
        references: {
          model: "chapter",
          key: "chapterId",
        },
      },
      purchasedAt: {
        type: Sequelize.DATE,
      },
    });
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable("purchase");
  },
};
