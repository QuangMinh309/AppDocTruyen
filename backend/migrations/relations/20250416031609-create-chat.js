'use strict';
/** @type {import('sequelize-cli').Migration} */
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('chats', {
      chatId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      communityId: {
        type: Sequelize.INTEGER,
        references: {
          model: 'communities',
          key: 'communityId'
        }
      },
      senderId: {
        type: Sequelize.INTEGER,
        references: {
          model: 'users',
          key: 'userId'
        }
      },
      content: {
        type: Sequelize.TEXT
      },
      commentPicURL: {
        type: Sequelize.STRING
      },
      time: {
        type: Sequelize.DATE
      }
    });
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable('chats');
  }
};