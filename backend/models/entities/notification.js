import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class Notification extends Sequelize.Model {}
  Notification.init(
    {
      notificationId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      type: {
        type: DataTypes.STRING,
      },
      content: {
        type: DataTypes.STRING,
      },
      refId: {
        type: DataTypes.INTEGER,
      },
      status: {
        type: DataTypes.STRING,
      },
      createAt: {
        type: DataTypes.DATE,
      },
    },
    {
      sequelize,
      modelName: "Notification",
      tableName: "notification",
      timestamps: false,
    }
  );

  Notification.associate = (models) => {};

  return Notification;
};
