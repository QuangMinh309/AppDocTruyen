'use strict';
/** @type {import('sequelize-cli').Migration} */
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('chapters', {
      chapterId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      chapterName: {
        type: Sequelize.STRING
      },
      storyId: {
        type: Sequelize.INTEGER,
        references: {
          model: 'stories',
          key: 'storyId'
        }
      },
      content: {
        type: Sequelize.TEXT('long')
      },
      viewNum: {
        type: Sequelize.INTEGER
      },
      lockedStatus: {
        type: Sequelize.BOOLEAN
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