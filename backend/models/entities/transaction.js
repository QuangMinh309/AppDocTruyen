import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class Transaction extends Model {
    static associate(models) {
      // Transaction belongs to User
      Transaction.belongsTo(models.User, {
        foreignKey: 'userId',
        as: 'user',
      })
    }
  }

  Transaction.init(
    {
      transactionId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
      },
      userId: DataTypes.INTEGER,
      money: DataTypes.INTEGER,
      type: DataTypes.STRING,
      time: DataTypes.DATE,
      status: DataTypes.STRING,
      finishAt: DataTypes.DATE,
    },
    {
      sequelize,
      modelName: 'Transaction',
      tableName: 'transaction',
      timestamps: false,
    }
  )

  return Transaction
}
