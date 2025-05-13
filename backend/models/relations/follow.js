import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class Follow extends Sequelize.Model {}
  Follow.init(
    {
      followId: {
        allowNull: false,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      followedId: {
        allowNull: false,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
    },
    {
      sequelize,
      modelName: "Follow",
      tableName: "follow",
      timestamps: false,
    }
  );

  Follow.associate = (models) => {
    Follow.belongsTo(models.User, { foreignKey: "followId" });
    Follow.belongsTo(models.User, { foreignKey: "followedId" });
  };

  return Follow;
};
