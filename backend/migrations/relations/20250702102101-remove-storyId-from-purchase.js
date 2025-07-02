/** @type {import('sequelize-cli').Migration} */
export default {
  async up(queryInterface, Sequelize) {
    await queryInterface.removeColumn('purchase', 'storyId');
  },

  async down(queryInterface, Sequelize) {
    await queryInterface.addColumn('purchase', 'storyId', {
      type: Sequelize.INTEGER,
      references: {
        model: 'story',
        key: 'storyId',
      },
      onUpdate: 'CASCADE',
      onDelete: 'SET NULL',
    });
  },
};
