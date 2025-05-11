import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class PasswordReset extends Sequelize.Model {}
  PasswordReset.init(
    {
      OTP: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      userId: {
        type: DataTypes.INTEGER,
      },
      isUsed: {
        type: DataTypes.BOOLEAN,
      },
    },
    {
      sequelize,
      modelName: "PasswordReset",
      tableName: "password_reset",
      timestamps: true,
      createdAt: true,
      updatedAt: false,
    }
  );

  PasswordReset.associate = (models) => {
    PasswordReset.belongsTo(models.User, { foreignKey: "userId" });
  };

  return PasswordReset;
};
