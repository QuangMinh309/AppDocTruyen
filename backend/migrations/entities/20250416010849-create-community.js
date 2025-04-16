'use strict';
/** @type {import('sequelize-cli').Migration} */
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('communities', {
      id: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      communityId: {
        type: Sequelize.INTEGER
      },
      communitytName: {
        type: Sequelize.STRING
      },
      categoryId: {
        type: Sequelize.INTEGER
      },
      menberNum: {
        type: Sequelize.INTEGER
      },
      description: {
        type: Sequelize.STRING
      },
      createdAt: {
        type: Sequelize.DATE
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
    await queryInterface.dropTable('communities');
  }
};