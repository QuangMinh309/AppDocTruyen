import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class NameList extends Model {
    static associate(models) {
      // NameList belongs to User
      NameList.belongsTo(models.User, {
        foreignKey: 'userId',
        as: 'user',
      })

      // NameList has many Stories (many-to-many)
      NameList.belongsToMany(models.Story, {
        through: models.ReadList,
        foreignKey: 'nameListId',
        otherKey: 'storyId',
        as: 'stories',
      })
    }
  }

  NameList.init(
    {
      nameListId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
      },
      nameList: DataTypes.STRING(255),
      userId: DataTypes.INTEGER,
      description: DataTypes.STRING(1500),
    },
    {
      sequelize,
      modelName: 'NameList',
      tableName: 'name_list',
      timestamps: false,
    }
  )

  return NameList
}

