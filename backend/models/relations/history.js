import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class History extends Model {
    static associate(models) {
      // History belongs to User
      History.belongsTo(models.User, {
        foreignKey: 'userId',
        as: 'user',
      })

      // History belongs to story
      History.belongsTo(models.Story, {
        foreignKey: 'storyId',
        as: 'story',
      })
    }
  }

  History.init(
    {
      historyId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
      },
      userId: DataTypes.INTEGER,
      storyId: DataTypes.INTEGER,
      lastReadAt: DataTypes.DATE,
    },
    {
      sequelize,
      modelName: 'History',
      tableName: 'history',
      timestamps: false,
    }
  )

  return History
}
