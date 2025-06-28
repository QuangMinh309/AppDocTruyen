/** @type {import('sequelize-cli').Migration} */
export default {
  async up(queryInterface, Sequelize) {
    await queryInterface.removeColumn('story', 'price');
  },

  async down(queryInterface, Sequelize) {
    await queryInterface.addColumn('story', 'price', {
      type: Sequelize.FLOAT,
    });
  },
};
