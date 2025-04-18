'use strict';
/** @type {import('sequelize-cli').Migration} */
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('users', {
      userId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      userName: {
        type: Sequelize.STRING
      },
      roleId: {
        type: Sequelize.INTEGER,
        references: {
          model: 'roles',
          key: 'roleId'
        }
      },
      dUserName: {
        type: Sequelize.STRING
      },
      mail: {
        type: Sequelize.STRING
      },
      about:{
        type:Sequelize.STRING(1500)
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
      }
    });
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable('users');
  }
};