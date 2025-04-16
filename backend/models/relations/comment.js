'use strict';
const {
  Model
} = require('sequelize');
module.exports = (sequelize, DataTypes) => {
  class comment extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models) {
      // define association here
    }
  }
  comment.init({
    commentId: DataTypes.INTEGER,
    userId: DataTypes.INTEGER,
    chapterId: DataTypes.INTEGER,
    content: DataTypes.TEXT,
    updateAt: DataTypes.DATE
  }, {
    sequelize,
    modelName: 'comment',
  });
  return comment;
};