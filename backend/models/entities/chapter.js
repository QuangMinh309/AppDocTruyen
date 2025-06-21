import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class Chapter extends Model {
    static associate(models) {
      // Chapter belongs to Story
      Chapter.belongsTo(models.Story, {
        foreignKey: 'storyId',
        as: 'story',
      })

      // Chapter has many Comments
      Chapter.hasMany(models.Comment, {
        foreignKey: 'chapterId',
        as: 'comments',
      })

      // Chapter has many Purchases
      Chapter.hasMany(models.Purchase, {
        foreignKey: 'chapterId',
        as: 'purchases',
      })

      // Chapter has many History entries
      Chapter.hasMany(models.History, {
        foreignKey: 'chapterId',
        as: 'histories',
      })
    }
  }

  Chapter.init(
    {
      chapterId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
      },
      chapterName: DataTypes.STRING,
      ordinalNumber: DataTypes.INTEGER,
      storyId: DataTypes.INTEGER,
      content: DataTypes.TEXT('long'),
      viewNum: DataTypes.INTEGER,
      lockedStatus: DataTypes.BOOLEAN,
      updatedAt: {
        type: DataTypes.DATE,
        allowNull: false,
      },
    },
    {
      sequelize,
      modelName: 'Chapter',
      tableName: 'chapter',
      timestamps: false, // Only updatedAt exists
    }
  )

  return Chapter
}
