'use strict';
/** @type {import('sequelize-cli').Migration} */
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('favor_lists', {
      favorId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      listName: {
        type: Sequelize.STRING
      },
      storyId: {
        type: Sequelize.INTEGER,
        references: {
          model: 'stories',
          key: 'storyId'
        }
      },
      userId: {
        type: Sequelize.INTEGER,
        references: {
          model: 'users',
          key: 'userId'
        }
      },
      description: {
        type: Sequelize.STRING(1500)
      }
    });
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable('favor_lists');
  }
};