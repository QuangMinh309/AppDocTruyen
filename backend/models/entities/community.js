'use strict';
const {
  Model
} = require('sequelize');
module.exports = (sequelize, DataTypes) => {
  class community extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models) {
      // define association here
    }
  }
  community.init({
    communityId: DataTypes.INTEGER,
    communitytName: DataTypes.STRING,
    categoryId: DataTypes.INTEGER,
    avatarId: DataTypes.STRING,
    menberNum: DataTypes.INTEGER,
    description: DataTypes.STRING(1500)
  }, {
    sequelize,
    modelName: 'community',
  });
  return community;
};