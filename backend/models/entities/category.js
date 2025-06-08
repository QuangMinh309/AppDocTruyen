import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class Category extends Model {
    static associate(models) {
      // Category has many Communities
      Category.hasMany(models.Community, {
        foreignKey: 'categoryId',
        as: 'communities',
      })

      // Category has many Stories (many-to-many)
      Category.belongsToMany(models.Story, {
        through: models.StoryCategory,
        foreignKey: 'categoryId',
        otherKey: 'storyId',
        as: 'stories',
      })
    }
  }

  Category.init(
    {
      categoryId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
      },
      categoryName: DataTypes.STRING,
    },
    {
      sequelize,
      modelName: 'Category',
      tableName: 'category',
      timestamps: false,
    }
  )

  return Category
}
