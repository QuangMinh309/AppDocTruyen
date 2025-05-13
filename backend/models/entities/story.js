import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class Story extends Sequelize.Model {}
  Story.init(
    {
      storyId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      storyName: {
        type: DataTypes.STRING,
      },
      userId: {
        type: DataTypes.INTEGER,
      },
      description: {
        type: DataTypes.STRING(1500),
      },
      ageRange: {
        type: DataTypes.INTEGER,
      },
      viewNum: {
        type: DataTypes.INTEGER,
      },
      voteNum: {
        type: DataTypes.INTEGER,
      },
      chapterNum: {
        type: DataTypes.INTEGER,
      },
      status: {
        type: DataTypes.STRING,
      },
      price: {
        type: DataTypes.FLOAT,
      },
      pricePerChapter: {
        type: DataTypes.FLOAT,
      },
      coverImgId: {
        type: DataTypes.STRING,
      },
    },
    {
      sequelize,
      modelName: "Story",
      tableName: "story",
      timestamps: true,
      createdAt: true,
      updatedAt: true,
    }
  );

  Story.associate = (models) => {
    Story.belongsTo(models.User, { foreignKey: "userId" });
    Story.hasMany(models.Chapter, { foreignKey: "storyId" });
    Story.hasMany(models.StoryCategory, { foreignKey: "storyId" });
    Story.hasMany(models.Vote, { foreignKey: "storyId" });
    Story.hasMany(models.Purchase, { foreignKey: "storyId" });
    Story.hasMany(models.ReadList, { foreignKey: "storyId" });
  };

  return Story;
};
