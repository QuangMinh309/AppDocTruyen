import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class ReadList extends Model {
    static associate(models) {
      // Junction table associations are defined in the related models
    }
  }

  ReadList.init(
    {
      storyId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        allowNull: false,
      },
      nameListId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        allowNull: false,
      },
    },
    {
      sequelize,
      modelName: 'ReadList',
      tableName: 'read_list',
      timestamps: false,
    }
  )

  return ReadList
}
