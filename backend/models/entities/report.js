import { Sequelize, DataTypes } from 'sequelize';

export default (sequelize) => {
  class Report extends Sequelize.Model {}
  Report.init(
    {
      reportId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      date: {
        type: DataTypes.STRING,
        allowNull: false,
      },
      totalIncome: {
        type: DataTypes.INTEGER,
        allowNull: false,
        defaultValue: 0,
      },
    },
    {
      sequelize,
      modelName: 'Report',
      tableName: 'report',
      timestamps: true,
    }
  );

  return Report;
};
