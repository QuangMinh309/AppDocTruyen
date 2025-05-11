import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class Category extends Sequelize.Model {}
  Category.init(
    {
      categoryId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      categoryName: {
        allowNull: false,
        type: DataTypes.STRING,
      },
    },
    {
      sequelize,
      modelName: "Category",
      tableName: "category",
      timestamps: false,
    }
  );

  Category.associate = (models) => {
    Category.hasMany(models.Community, { foreignKey: "categoryId" });
    Category.hasMany(models.StoryCategory, { foreignKey: "categoryId" });
  };

  return Category;
};
