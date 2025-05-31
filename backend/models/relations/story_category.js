import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class StoryCategory extends Model {
    static associate(models) {
      // Junction table associations are defined in the related models
    }
  }

  StoryCategory.init(
    {
      storyId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        allowNull: false,
      },
      categoryId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        allowNull: false,
      },
    },
    {
      sequelize,
      modelName: 'StoryCategory',
      tableName: 'story_category',
      timestamps: false,
    }
  )

  return StoryCategory
}
