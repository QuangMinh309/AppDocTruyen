import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class Community extends Model {
    static associate(models) {
      // Community belongs to Category
      Community.belongsTo(models.Category, {
        foreignKey: 'categoryId',
        as: 'category',
      })

      // Community has many Chats
      Community.hasMany(models.Chat, {
        foreignKey: 'communityId',
        as: 'chats',
      })

      // Community has many Users (many-to-many)
      Community.belongsToMany(models.User, {
        through: models.JoinCommunity,
        foreignKey: 'communityId',
        otherKey: 'userId',
        as: 'members',
      })
    }
  }

  Community.init(
    {
      communityId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
      },
      communityName: DataTypes.STRING,
      categoryId: DataTypes.INTEGER,
      avatarId: DataTypes.STRING,
      memberNum: DataTypes.INTEGER,
      description: DataTypes.STRING(1500),
    },
    {
      sequelize,
      modelName: 'Community',
      tableName: 'community',
      timestamps: false,
    }
  )

  return Community
}
