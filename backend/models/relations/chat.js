import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class Chat extends Sequelize.Model {}
  Chat.init(
    {
      chatId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      communityId: {
        type: DataTypes.INTEGER,
      },
      senderId: {
        type: DataTypes.INTEGER,
      },
      content: {
        type: DataTypes.TEXT,
      },
      commentPicId: {
        type: DataTypes.STRING,
      },
      time: {
        type: DataTypes.DATE,
      },
    },
    {
      sequelize,
      modelName: "Chat",
      tableName: "chat",
      timestamps: false,
    }
  );

  Chat.associate = (models) => {
    Chat.belongsTo(models.Community, { foreignKey: "communityId" });
    Chat.belongsTo(models.User, { foreignKey: "senderId" });
  };

  return Chat;
};
