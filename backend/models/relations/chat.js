'use strict';
const {
  Model
} = require('sequelize');
module.exports = (sequelize, DataTypes) => {
  class chat extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models) {
      // define association here
    }
  }
  chat.init({
    chatId: DataTypes.INTEGER,
    communityId: DataTypes.INTEGER,
    senderId: DataTypes.INTEGER,
    content: DataTypes.TEXT,
    commentPicId: DataTypes.STRING,
    time: DataTypes.DATE
  }, {
    sequelize,
    modelName: 'chat',
  });
  return chat;
};