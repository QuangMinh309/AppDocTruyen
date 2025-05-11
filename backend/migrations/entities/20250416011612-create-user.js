/** @type {import('sequelize-cli').Migration} */
export default {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable("user", {
      userId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER,
      },
      userName: {
        type: Sequelize.STRING,
      },
      roleId: {
        type: Sequelize.INTEGER,
        references: {
          model: "role",
          key: "roleId",
        },
      },
      dUserName: {
        type: Sequelize.STRING,
      },
      mail: {
        type: Sequelize.STRING,
      },
      about: {
        type: Sequelize.STRING(1500),
      },
      DOB: {
        type: Sequelize.DATE,
      },
      followerNum: {
        type: Sequelize.INTEGER,
      },
      password: {
        type: Sequelize.STRING,
      },
      avatarId: {
        type: Sequelize.STRING,
      },
      backgroundId: {
        type: Sequelize.STRING,
      },
      wallet: {
        type: Sequelize.DECIMAL(15, 2),
      },
      isPremium: {
        type: Sequelize.BOOLEAN,
      },
    });
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable("user");
  },
};
