'use strict';
/** @type {import('sequelize-cli').Migration} */
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('stories', {
      storyId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      storyName: {
        type: Sequelize.STRING
      },
      userId: {
        type: Sequelize.INTEGER,
      references: {
          model: 'users',
          key: 'userId'
        }
      },
      tittle: {
        type: Sequelize.STRING
      },
      description: {
        type: Sequelize.STRING(1500)
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
      chapterNum: {
        type: Sequelize.INTEGER
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