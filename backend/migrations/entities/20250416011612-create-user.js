'use strict';
/** @type {import('sequelize-cli').Migration} */
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('users', {
      id: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      userId: {
        type: Sequelize.INTEGER
      },
      userName: {
        type: Sequelize.STRING
      },
      roleId: {
        type: Sequelize.INTEGER
      },
      dUserName: {
        type: Sequelize.STRING
      },
      mail: {
        type: Sequelize.STRING
      },
      DOB: {
        type: Sequelize.DATE
      },
      followerNum: {
        type: Sequelize.INTEGER
      },
      password: {
        type: Sequelize.STRING
      },
      avatarURL: {
        type: Sequelize.STRING
      },
      isPremium: {
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
    await queryInterface.dropTable('users');
  }
};