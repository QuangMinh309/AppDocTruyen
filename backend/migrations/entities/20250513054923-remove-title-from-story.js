/** @type {import('sequelize-cli').Migration} */
export default {
  async up(queryInterface, Sequelize) {
    await queryInterface.removeColumn("story", "title");
  },

  async down(queryInterface, Sequelize) {
    await queryInterface.addColumn("story", "title", {
      type: Sequelize.STRING,
    });
  },
};
