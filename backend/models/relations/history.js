import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class History extends Sequelize.Model {}
  History.init(
    {
      historyId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      userId: {
        type: DataTypes.INTEGER,
      },
      chapterId: {
        type: DataTypes.INTEGER,
      },
      lastReadAt: {
        type: DataTypes.DATE,
      },
    },
    {
      sequelize,
      modelName: "History",
      tableName: "history",
      timestamps: false,
    }
  );

  History.associate = (models) => {
    History.belongsTo(models.User, { foreignKey: "userId" });
    History.belongsTo(models.Chapter, { foreignKey: "chapterId" });
  };

  return History;
};
