'use strict';
const {
  Model
} = require('sequelize');
module.exports = (sequelize, DataTypes) => {
  class story extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models) {
      // define association here
    }
  }
  story.init({
    storyId: DataTypes.INTEGER,
    storyName: DataTypes.STRING,
    userId: DataTypes.INTEGER,
    title: DataTypes.STRING,
    description: DataTypes.STRING(1500),
    ageRange: DataTypes.INTEGER,
    viewNum: DataTypes.INTEGER,
    voteNum: DataTypes.INTEGER,
    createdAt: DataTypes.DATE,
    updateAt: DataTypes.DATE,
    status: DataTypes.STRING,
    price: DataTypes.FLOAT,
    pricePerChapter: DataTypes.FLOAT,
    coverImgId: DataTypes.STRING
  }, {
    sequelize,
    modelName: 'story',
  });
  return story;
};