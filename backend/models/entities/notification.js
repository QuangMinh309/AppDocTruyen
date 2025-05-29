import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class Notification extends Model {
    static associate(models) {
      Notification.belongsTo(models.User, {
        foreignKey: 'userId',
        as: 'user',
      })
    }
  }

  Notification.init(
    {
      notificationId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
      },
      userId: DataTypes.INTEGER,
      type: DataTypes.STRING,
      content: DataTypes.STRING,
      refId: DataTypes.INTEGER,
      status: DataTypes.STRING,
      createAt: DataTypes.DATE,
    },
    {
      sequelize,
      modelName: 'Notification',
      tableName: 'notification',
      timestamps: false,
    }
  )

  return Notification
}
