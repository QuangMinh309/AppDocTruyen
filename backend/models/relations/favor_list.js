'use strict';
const {
  Model
} = require('sequelize');
module.exports = (sequelize, DataTypes) => {
  class favor_list extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models) {
      // define association here
    }
  }
  favor_list.init({
    favorId: DataTypes.INTEGER,
    listName: DataTypes.STRING,
    storyId: DataTypes.INTEGER,
    userId: DataTypes.INTEGER,
    description: DataTypes.STRING
  }, {
    sequelize,
    modelName: 'favor_list',
  });
  return favor_list;
};