import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class User extends Sequelize.Model {}
  User.init(
    {
      userId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      userName: {
        type: DataTypes.STRING,
      },
      roleId: {
        type: DataTypes.INTEGER,
      },
      dUserName: {
        type: DataTypes.STRING,
      },
      mail: {
        type: DataTypes.STRING,
      },
      about: {
        type: DataTypes.STRING(1500),
      },
      DOB: {
        type: DataTypes.DATE,
      },
      followerNum: {
        type: DataTypes.INTEGER,
      },
      password: {
        type: DataTypes.STRING,
      },
      avatarId: {
        type: DataTypes.STRING,
      },
      backgroundId: {
        type: DataTypes.STRING,
      },
      wallet: {
        type: DataTypes.DECIMAL(15, 2),
      },
      isPremium: {
        type: DataTypes.BOOLEAN,
      },
    },
    {
      sequelize,
      modelName: "User",
      tableName: "user",
      timestamps: false,
    }
  );

  User.associate = (models) => {
    User.belongsTo(models.Role, { foreignKey: "roleId" });
    User.hasMany(models.Story, { foreignKey: "userId" });
    User.hasMany(models.PasswordReset, { foreignKey: "userId" });
    User.hasMany(models.Transaction, { foreignKey: "userId" });
    User.hasMany(models.Vote, { foreignKey: "userId" });
    User.hasMany(models.Chat, { foreignKey: "senderId" });
    User.hasMany(models.Follow, { foreignKey: "followId" });
    User.hasMany(models.Follow, { foreignKey: "followedId" });
    User.hasMany(models.Purchase, { foreignKey: "userId" });
    User.hasMany(models.History, { foreignKey: "userId" });
    User.hasMany(models.Comment, { foreignKey: "userId" });
    User.hasMany(models.Premium, { foreignKey: "userId" });
    User.hasMany(models.JoinCommunity, { foreignKey: "userId" });
    User.hasMany(models.NameList, { foreignKey: "userId" });
    User.hasMany(models.LikeComment, { foreignKey: "userId" });
  };

  return User;
};
