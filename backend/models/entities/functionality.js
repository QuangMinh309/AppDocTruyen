'use strict';
const {
  Model
} = require('sequelize');
module.exports = (sequelize, DataTypes) => {
  class functionality extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models) {
      // define association here
    }
  }
  functionality.init({
    funcId: DataTypes.INTEGER,
    funcName: DataTypes.STRING,
    funcViewId: DataTypes.STRING
  }, {
    sequelize,
    modelName: 'functionality',
  });
  return functionality;
};