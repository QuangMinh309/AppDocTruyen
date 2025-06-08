import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class Follow extends Model {
    static associate(models) {
      // Junction table associations are defined in the related models
    }
  }

  Follow.init(
    {
      followId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        allowNull: false,
      },
      followedId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        allowNull: false,
      },
    },
    {
      sequelize,
      modelName: 'Follow',
      tableName: 'follow',
      timestamps: false,
    }
  )

  return Follow
}
