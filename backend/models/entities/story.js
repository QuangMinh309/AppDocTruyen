import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class Story extends Model {
    static associate(models) {
      // Story belongs to User
      Story.belongsTo(models.User, {
        foreignKey: 'userId',
        as: 'author',
      })

      // Story has many Chapters
      Story.hasMany(models.Chapter, {
        foreignKey: 'storyId',
        as: 'chapters',
      })

      // Story has many Purchases
      Story.hasMany(models.Purchase, {
        foreignKey: 'storyId',
        as: 'purchases',
      })

      // Story belongs to many Categories (many-to-many)
      Story.belongsToMany(models.Category, {
        through: models.StoryCategory,
        foreignKey: 'storyId',
        otherKey: 'categoryId',
        as: 'categories',
      })

      // Story has many Votes from Users (many-to-many)
      Story.belongsToMany(models.User, {
        through: models.Vote,
        foreignKey: 'storyId',
        otherKey: 'userId',
        as: 'voters',
      })

      // Story belongs to many NameLists/ReadLists (many-to-many)
      Story.belongsToMany(models.NameList, {
        through: models.ReadList,
        foreignKey: 'storyId',
        otherKey: 'nameListId',
        as: 'nameLists',
      })
    }
  }

  Story.init(
    {
      storyId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
      },
      storyName: DataTypes.STRING,
      userId: DataTypes.INTEGER,
      description: DataTypes.STRING(1500),
      ageRange: DataTypes.INTEGER,
      viewNum: DataTypes.INTEGER,
      voteNum: DataTypes.INTEGER,
      chapterNum: DataTypes.INTEGER,
      status: DataTypes.STRING,
      pricePerChapter: DataTypes.FLOAT,
      coverImgId: DataTypes.STRING,
      createdAt: {
        type: DataTypes.DATE,
        allowNull: false,
      },
      updatedAt: {
        type: DataTypes.DATE,
        allowNull: false,
      },
    },
    {
      sequelize,
      modelName: 'Story',
      tableName: 'story',
      timestamps: true,
    }
  )

  return Story
}
