import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class LikeComment extends Model {
    static associate(models) {
      // Junction table associations are defined in the related models
    }
  }

  LikeComment.init(
    {
      commentId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        allowNull: false,
      },
      userId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        allowNull: false,
      },
    },
    {
      sequelize,
      modelName: 'LikeComment',
      tableName: 'like_comment',
      timestamps: false,
    }
  )

  return LikeComment
}
