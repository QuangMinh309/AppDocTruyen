'use strict';
/** @type {import('sequelize-cli').Migration} */
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('community', {
      communityId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      communitytName: {
        type: Sequelize.STRING
      },
      categoryId: {
        type: Sequelize.INTEGER,
        references: 
        {
          model: 'category',
          key: 'categoryId'
        }
      },
      menberNum: {
        type: Sequelize.INTEGER
      },
      description: {
        type: Sequelize.STRING(1500)
      },
    });
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable('community');
  }
};