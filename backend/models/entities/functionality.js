import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class Functionality extends Sequelize.Model {}
  Functionality.init(
    {
      funcId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      funcName: {
        type: DataTypes.STRING,
      },
      funcViewId: {
        type: DataTypes.STRING,
      },
    },
    {
      sequelize,
      modelName: "Functionality",
      tableName: "functionality",
      timestamps: false,
    }
  );

  Functionality.associate = (models) => {
    Functionality.hasMany(models.Authorization, { foreignKey: "funcId" });
  };

  return Functionality;
};
