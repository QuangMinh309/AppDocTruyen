/** @type {import('sequelize-cli').Migration} */
export default {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable("chat", {
      chatId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER,
      },
      communityId: {
        type: Sequelize.INTEGER,
        references: {
          model: "community",
          key: "communityId",
        },
      },
      senderId: {
        type: Sequelize.INTEGER,
        references: {
          model: "user",
          key: "userId",
        },
      },
      content: {
        type: Sequelize.TEXT,
      },
      commentPicId: {
        type: Sequelize.STRING,
      },
      time: {
        type: Sequelize.DATE,
      },
    });
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable("chat");
  },
};
