/** @type {import('sequelize-cli').Migration} */
export default {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable("comment", {
      commentId: {
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
      chapterId: {
        type: Sequelize.INTEGER,
        references: {
          model: "chapter",
          key: "chapterId",
        },
      },
      commentPicId: {
        type: Sequelize.STRING,
      },
      content: {
        type: Sequelize.TEXT,
      },
      createAt: {
        type: Sequelize.DATE,
      },
    });
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable("comment");
  },
};
