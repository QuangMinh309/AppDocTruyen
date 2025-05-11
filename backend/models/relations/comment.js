import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class Comment extends Sequelize.Model {}
  Comment.init(
    {
      commentId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      userId: {
        type: DataTypes.INTEGER,
      },
      chapterId: {
        type: DataTypes.INTEGER,
      },
      commentPicId: {
        type: DataTypes.STRING,
      },
      content: {
        type: DataTypes.TEXT,
      },
      createAt: {
        type: DataTypes.DATE,
      },
    },
    {
      sequelize,
      modelName: "Comment",
      tableName: "comment",
      timestamps: false,
    }
  );

  Comment.associate = (models) => {
    Comment.belongsTo(models.User, { foreignKey: "userId" });
    Comment.belongsTo(models.Chapter, { foreignKey: "chapterId" });
    Comment.hasMany(models.LikeComment, { foreignKey: "commentId" });
  };

  return Comment;
};
