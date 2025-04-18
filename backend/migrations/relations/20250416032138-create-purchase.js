'use strict';
/** @type {import('sequelize-cli').Migration} */
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('purchases', {
      purchasedId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      userId: {
        type: Sequelize.INTEGER,
        references: {
          model: 'users',
          key: 'userId'
        }
      },
      storyId: {
        type: Sequelize.INTEGER,
        references: {
          model: 'stories',
          key: 'storyId'
        }
      },
      chapterId: {
        type: Sequelize.INTEGER,
        references: {
          model: 'chapters',
          key: 'chapterId'
        }
      },
      purchasedAt: {
        type: Sequelize.DATE
      }
    });
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable('purchases');
  }
};