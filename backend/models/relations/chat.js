import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class Chat extends Model {
    static associate(models) {
      // Chat belongs to Community
      Chat.belongsTo(models.Community, {
        foreignKey: 'communityId',
        as: 'community',
      })

      // Chat belongs to User (sender)
      Chat.belongsTo(models.User, {
        foreignKey: 'senderId',
        as: 'sender',
      })
    }
  }

  Chat.init(
    {
      chatId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
      },
      communityId: DataTypes.INTEGER,
      senderId: DataTypes.INTEGER,
      content: DataTypes.TEXT,
      commentPicId: DataTypes.STRING,
      time: {
        type: DataTypes.DATE,
        allowNull: false,
      }
    },
    {
      sequelize,
      modelName: 'Chat',
      tableName: 'chat',
      timestamps: false,
    }
  )

  return Chat
}
