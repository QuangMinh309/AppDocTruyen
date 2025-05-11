import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class Community extends Sequelize.Model {}
  Community.init(
    {
      communityId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      communitytName: {
        type: DataTypes.STRING,
      },
      categoryId: {
        type: DataTypes.INTEGER,
      },
      avatarId: {
        type: DataTypes.STRING,
      },
      menberNum: {
        type: DataTypes.INTEGER,
      },
      description: {
        type: DataTypes.STRING(1500),
      },
    },
    {
      sequelize,
      modelName: "Community",
      tableName: "community",
      timestamps: false,
    }
  );

  Community.associate = (models) => {
    Community.belongsTo(models.Category, { foreignKey: "categoryId" });
    Community.hasMany(models.Chat, { foreignKey: "communityId" });
    Community.hasMany(models.JoinCommunity, { foreignKey: "communityId" });
  };

  return Community;
};
