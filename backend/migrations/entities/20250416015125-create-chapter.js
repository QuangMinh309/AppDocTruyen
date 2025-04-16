'use strict';
/** @type {import('sequelize-cli').Migration} */
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('chapters', {
      id: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      chapterId: {
        type: Sequelize.INTEGER
      },
      chapterName: {
        type: Sequelize.STRING
      },
      storyId: {
        type: Sequelize.INTEGER
      },
      content: {
        type: Sequelize.TEXT
      },
      viewNum: {
        type: Sequelize.INTEGER
      },
      UpdateAt: {
        type: Sequelize.DATE
      },
      lockedStatus: {
        type: Sequelize.BOOLEAN
      },
      createdAt: {
        allowNull: false,
        type: Sequelize.DATE
      },
      updatedAt: {
        allowNull: false,
        type: Sequelize.DATE
      }
    });
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable('chapters');
  }
};