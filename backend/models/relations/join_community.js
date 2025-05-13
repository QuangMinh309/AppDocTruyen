import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class JoinCommunity extends Sequelize.Model {}
  JoinCommunity.init(
    {
      userId: {
        allowNull: false,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      communityId: {
        allowNull: false,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
    },
    {
      sequelize,
      modelName: "JoinCommunity",
      tableName: "join_community",
      timestamps: false,
    }
  );

  JoinCommunity.associate = (models) => {
    JoinCommunity.belongsTo(models.User, { foreignKey: "userId" });
    JoinCommunity.belongsTo(models.Community, { foreignKey: "communityId" });
  };

  return JoinCommunity;
};
