import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class Vote extends Model {
    static associate(models) {
      // Junction table associations are defined in the related models
    }
  }

  Vote.init(
    {
      userId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        allowNull: false,
      },
      storyId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        allowNull: false,
      },
    },
    {
      sequelize,
      modelName: 'Vote',
      tableName: 'vote',
      timestamps: false,
    }
  )

  return Vote
}
