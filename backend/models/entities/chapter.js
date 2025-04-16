'use strict';
const {
  Model
} = require('sequelize');
module.exports = (sequelize, DataTypes) => {
  class chapter extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models) {
      // define association here
    }
  }
  chapter.init({
    chapterId: DataTypes.INTEGER,
    chapterName: DataTypes.STRING,
    storyId: DataTypes.INTEGER,
    content: DataTypes.TEXT,
    viewNum: DataTypes.INTEGER,
    UpdateAt: DataTypes.DATE,
    lockedStatus: DataTypes.BOOLEAN
  }, {
    sequelize,
    modelName: 'chapter',
  });
  return chapter;
};