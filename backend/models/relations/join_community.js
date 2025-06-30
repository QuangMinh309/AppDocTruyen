import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class JoinCommunity extends Model {
    static associate(models) {
      // Junction table associations are defined in the related models
    }
  }

  JoinCommunity.init(
    {
      userId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        allowNull: false,
      },
      communityId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        allowNull: false,
      },
    },
    {
      sequelize,
      modelName: 'JoinCommunity',
      tableName: 'join_community',
      timestamps: false,
    }
  )

  return JoinCommunity
}
