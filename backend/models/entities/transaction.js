'use strict';
const {
  Model
} = require('sequelize');
module.exports = (sequelize, DataTypes) => {
  class transaction extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models) {
      // define association here
    }
  }
  transaction.init({
    transactionId: DataTypes.INTEGER,
    userId: DataTypes.INTEGER,
    money: DataTypes.INTEGER,
    type: DataTypes.STRING,
    time: DataTypes.DATE,
    status: DataTypes.STRING,
    finishAt: DataTypes.DATE
  }, {
    sequelize,
    modelName: 'transaction',
  });
  return transaction;
};