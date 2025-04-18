'use strict';
/** @type {import('sequelize-cli').Migration} */
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('authorizations', {
      roleId: {
        allowNull: false,
        primaryKey: true,
        type: Sequelize.INTEGER,
        references: 
        {
          model: 'roles',
          key: 'roleId'
        }
      },
      funcId: {
        allowNull: false,
        primaryKey: true,
        type: Sequelize.INTEGER,
        references: 
        {
          model: 'functionalities',
          key: 'funcId'
        }
      }
    });
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable('authorizations');
  }
};