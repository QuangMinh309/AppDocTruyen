'use strict';
/** @type {import('sequelize-cli').Migration} */
export default {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('notification', {
      notificationId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      type: {
        type: Sequelize.STRING
      },
      content: {
        type: Sequelize.STRING
      },
      refId: {
        type: Sequelize.INTEGER
      },
      status: {
        type: Sequelize.STRING
      },
      createAt: {
        type: Sequelize.DATE
      }
    });
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable('notification');
  }
};