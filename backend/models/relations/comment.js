import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class Comment extends Model {
    static associate(models) {
      // Comment belongs to User
      Comment.belongsTo(models.User, {
        foreignKey: 'userId',
        as: 'user',
      })

      // Comment belongs to Chapter
      Comment.belongsTo(models.Chapter, {
        foreignKey: 'chapterId',
        as: 'chapter',
      })

      // Comment has many likes from Users (many-to-many)
      Comment.belongsToMany(models.User, {
        through: models.LikeComment,
        foreignKey: 'commentId',
        otherKey: 'userId',
        as: 'likedBy',
      })
    }
  }

  Comment.init(
    {
      commentId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
      },
      userId: DataTypes.INTEGER,
      chapterId: DataTypes.INTEGER,
      commentPicId: DataTypes.STRING,
      content: DataTypes.TEXT,
      createAt: DataTypes.DATE,
    },
    {
      sequelize,
      modelName: 'Comment',
      tableName: 'comment',
      timestamps: false,
    }
  )

  return Comment
}
