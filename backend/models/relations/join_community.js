'use strict';
const {
  Model
} = require('sequelize');
module.exports = (sequelize, DataTypes) => {
  class join - community extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models) {
      // define association here
    }
  }
  join - community.init({
    userId: DataTypes.INTEGER,
    communityId: DataTypes.INTEGER
  }, {
    sequelize,
    modelName: 'join-community',
  });
  return join - community;
};