/** @type {import('sequelize-cli').Migration} */
export default {
  async up(queryInterface, Sequelize) {
    await queryInterface.addColumn("notification", "userId", {
      type: Sequelize.INTEGER,
      allowNull: false,
      references: {
        model: "user",
        key: "userId",
      },
      onUpdate: "CASCADE",
      onDelete: "CASCADE",
    });
  },

  async down(queryInterface, Sequelize) {
    await queryInterface.removeColumn("notification", "userId");
  },
};
