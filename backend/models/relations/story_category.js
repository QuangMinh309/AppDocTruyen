import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class StoryCategory extends Sequelize.Model {}
  StoryCategory.init(
    {
      storyId: {
        allowNull: false,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      categoryId: {
        allowNull: false,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
    },
    {
      sequelize,
      modelName: "StoryCategory",
      tableName: "story_category",
      timestamps: false,
    }
  );

  StoryCategory.associate = (models) => {
    StoryCategory.belongsTo(models.Story, { foreignKey: "storyId" });
    StoryCategory.belongsTo(models.Category, { foreignKey: "categoryId" });
  };

  return StoryCategory;
};
