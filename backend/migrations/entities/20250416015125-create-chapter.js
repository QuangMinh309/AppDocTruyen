/** @type {import('sequelize-cli').Migration} */
export default {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable("chapter", {
      chapterId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER,
      },
      chapterName: {
        type: Sequelize.STRING,
      },
      ordinalNumber: {
        type: Sequelize.INTEGER,
      },
      storyId: {
        type: Sequelize.INTEGER,
        references: {
          model: "story",
          key: "storyId",
        },
      },
      content: {
        type: Sequelize.TEXT("long"),
      },
      viewNum: {
        type: Sequelize.INTEGER,
      },
      lockedStatus: {
        type: Sequelize.BOOLEAN,
      },
      updatedAt: {
        allowNull: false,
        type: Sequelize.DATE,
      },
    });
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable("chapter");
  },
};
