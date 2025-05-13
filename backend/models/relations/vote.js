import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class Vote extends Sequelize.Model {}
  Vote.init(
    {
      userId: {
        allowNull: false,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      storyId: {
        allowNull: false,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
    },
    {
      sequelize,
      modelName: "Vote",
      tableName: "vote",
      timestamps: false,
    }
  );

  Vote.associate = (models) => {
    Vote.belongsTo(models.User, { foreignKey: "userId" });
    Vote.belongsTo(models.Story, { foreignKey: "storyId" });
  };

  return Vote;
};
