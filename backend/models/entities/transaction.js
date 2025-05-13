import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class Transaction extends Sequelize.Model {}
  Transaction.init(
    {
      transactionId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      userId: {
        type: DataTypes.INTEGER,
      },
      money: {
        type: DataTypes.INTEGER,
      },
      type: {
        type: DataTypes.STRING,
      },
      time: {
        type: DataTypes.DATE,
      },
      status: {
        type: DataTypes.STRING,
      },
      finishAt: {
        type: DataTypes.DATE,
      },
    },
    {
      sequelize,
      modelName: "Transaction",
      tableName: "transaction",
      timestamps: false,
    }
  );

  Transaction.associate = (models) => {
    Transaction.belongsTo(models.User, { foreignKey: "userId" });
  };

  return Transaction;
};
