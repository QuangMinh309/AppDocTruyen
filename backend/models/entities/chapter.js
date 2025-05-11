import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class Chapter extends Sequelize.Model {}
  Chapter.init(
    {
      chapterId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      chapterName: {
        type: DataTypes.STRING,
      },
      OrdinalNumber: {
        type: DataTypes.INTEGER,
      },
      storyId: {
        type: DataTypes.INTEGER,
      },
      content: {
        type: DataTypes.TEXT("long"),
      },
      viewNum: {
        type: DataTypes.INTEGER,
      },
      lockedStatus: {
        type: DataTypes.BOOLEAN,
      },
    },
    {
      sequelize,
      modelName: "Chapter",
      tableName: "chapter",
      timestamps: true,
      updatedAt: true,
      createdAt: false,
    }
  );

  Chapter.associate = (models) => {
    Chapter.belongsTo(models.Story, { foreignKey: "storyId" });
    Chapter.hasMany(models.Purchase, { foreignKey: "chapterId" });
    Chapter.hasMany(models.History, { foreignKey: "chapterId" });
    Chapter.hasMany(models.Comment, { foreignKey: "chapterId" });
  };

  return Chapter;
};
