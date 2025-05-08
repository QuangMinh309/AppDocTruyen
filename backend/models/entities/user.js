'use strict';
const {
  Model
} = require('sequelize');
module.exports = (sequelize, DataTypes) => {
  class user extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models) {
      // define association here
    }
  }
  user.init({
    userId: DataTypes.INTEGER,
    userName: DataTypes.STRING,
    roleId: DataTypes.INTEGER,
    dUserName: DataTypes.STRING,
    mail: DataTypes.STRING,
    about: DataTypes.STRING(1500),
    DOB: DataTypes.DATE,
    followerNum: DataTypes.INTEGER,
    password: DataTypes.STRING,
    avatarId: DataTypes.STRING,
    backgroundId: DataTypes.STRING,
    wallet: DataTypes.DECIMAL(15, 2),
    isPremium: DataTypes.BOOLEAN
  }, {
    sequelize,
    modelName: 'user',
  });
  return user;
};