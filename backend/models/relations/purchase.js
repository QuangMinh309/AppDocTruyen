import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class Purchase extends Sequelize.Model {}
  Purchase.init(
    {
      purchasedId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      userId: {
        type: DataTypes.INTEGER,
      },
      storyId: {
        type: DataTypes.INTEGER,
      },
      chapterId: {
        type: DataTypes.INTEGER,
      },
      purchasedAt: {
        type: DataTypes.DATE,
      },
    },
    {
      sequelize,
      modelName: "Purchase",
      tableName: "purchase",
      timestamps: false,
    }
  );

  Purchase.associate = (models) => {
    Purchase.belongsTo(models.User, { foreignKey: "userId" });
    Purchase.belongsTo(models.Story, { foreignKey: "storyId" });
    Purchase.belongsTo(models.Chapter, { foreignKey: "chapterId" });
  };

  return Purchase;
};
