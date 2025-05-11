import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class LikeComment extends Sequelize.Model {}
  LikeComment.init(
    {
      commentId: {
        allowNull: false,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      userId: {
        allowNull: false,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
    },
    {
      sequelize,
      modelName: "LikeComment",
      tableName: "like_comment",
      timestamps: false,
    }
  );

  LikeComment.associate = (models) => {
    LikeComment.belongsTo(models.Comment, { foreignKey: "commentId" });
    LikeComment.belongsTo(models.User, { foreignKey: "userId" });
  };

  return LikeComment;
};
