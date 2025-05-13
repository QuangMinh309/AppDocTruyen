import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class Authorization extends Sequelize.Model {}
  Authorization.init(
    {
      roleId: {
        allowNull: false,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      funcId: {
        allowNull: false,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
    },
    {
      sequelize,
      modelName: "Authorization",
      tableName: "authorization",
      timestamps: false,
    }
  );

  Authorization.associate = (models) => {
    Authorization.belongsTo(models.Role, { foreignKey: "roleId" });
    Authorization.belongsTo(models.Functionality, { foreignKey: "funcId" });
  };

  return Authorization;
};
