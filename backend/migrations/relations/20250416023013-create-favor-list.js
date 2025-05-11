/** @type {import('sequelize-cli').Migration} */
export default {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('favor_list', {
      favorId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      listName: {
        type: Sequelize.STRING
      },
      storyId: {
        type: Sequelize.INTEGER,
        references: {
          model: 'story',
          key: 'storyId'
        }
      },
      userId: {
        type: Sequelize.INTEGER,
        references: {
          model: 'user',
          key: 'userId'
        }
      },
      description: {
        type: Sequelize.STRING(1500)
      }
    });
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable('favor_list');
  }
};