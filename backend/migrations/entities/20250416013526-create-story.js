'use strict';
/** @type {import('sequelize-cli').Migration} */
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('stories', {
      id: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      storyId: {
        type: Sequelize.INTEGER
      },
      storyName: {
        type: Sequelize.STRING
      },
      userId: {
        type: Sequelize.INTEGER
      },
      tittle: {
        type: Sequelize.STRING
      },
      description: {
        type: Sequelize.STRING
      },
      ageRange: {
        type: Sequelize.INTEGER
      },
      viewNum: {
        type: Sequelize.INTEGER
      },
      voteNum: {
        type: Sequelize.INTEGER
      },
      createdAt: {
        type: Sequelize.DATE
      },
      updateAt: {
        type: Sequelize.DATE
      },
      status: {
        type: Sequelize.STRING
      },
      price: {
        type: Sequelize.FLOAT
      },
      pricePerChapter: {
        type: Sequelize.FLOAT
      },
      coverImgURL: {
        type: Sequelize.STRING
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
    await queryInterface.dropTable('stories');
  }
};