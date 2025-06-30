import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class User extends Model {
    static associate(models) {
      // User belongs to a Role
      User.belongsTo(models.Role, {
        foreignKey: 'roleId',
        as: 'role',
      })

      // User has many Stories
      User.hasMany(models.Story, {
        foreignKey: 'userId',
        as: 'stories',
      })

      // User has many Comments
      User.hasMany(models.Comment, {
        foreignKey: 'userId',
        as: 'comments',
      })

      // User has many PasswordResets
      User.hasMany(models.PasswordReset, {
        foreignKey: 'userId',
        as: 'passwordResets',
      })

      // User has many Transactions
      User.hasMany(models.Transaction, {
        foreignKey: 'userId',
        as: 'transactions',
      })

      // User has many NameLists
      User.hasMany(models.NameList, {
        foreignKey: 'userId',
        as: 'nameLists',
      })

      // User has many Purchases
      User.hasMany(models.Purchase, {
        foreignKey: 'userId',
        as: 'purchases',
      })

      // User has many History
      User.hasMany(models.History, {
        foreignKey: 'userId',
        as: 'histories',
      })

      // User has many Chats
      User.hasMany(models.Chat, {
        foreignKey: 'senderId',
        as: 'chats',
      })

      // User has Premium (one-to-one)
      User.hasOne(models.Premium, {
        foreignKey: 'userId',
        as: 'premium',
      })

      // User follows many Users (self-reference many-to-many)
      User.belongsToMany(models.User, {
        through: models.Follow,
        foreignKey: 'followId',
        otherKey: 'followedId',
        as: 'following',
      })

      // User has many followers (self-reference many-to-many)
      User.belongsToMany(models.User, {
        through: models.Follow,
        foreignKey: 'followedId',
        otherKey: 'followId',
        as: 'followers',
      })

      // User has many Votes (many-to-many with Story)
      User.belongsToMany(models.Story, {
        through: models.Vote,
        foreignKey: 'userId',
        otherKey: 'storyId',
        as: 'votedStories',
      })

      // User belongs to many Communities (many-to-many)
      User.belongsToMany(models.Community, {
        through: models.JoinCommunity,
        foreignKey: 'userId',
        otherKey: 'communityId',
        as: 'communities',
      })

      // User likes many Comments (many-to-many)
      User.belongsToMany(models.Comment, {
        through: models.LikeComment,
        foreignKey: 'userId',
        otherKey: 'commentId',
        as: 'likedComments',
      })
    }
  }

  User.init(
    {
      userId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
      },
      userName: DataTypes.STRING,
      roleId: DataTypes.INTEGER,
      dUserName: DataTypes.STRING,
      mail: DataTypes.STRING,
      about: DataTypes.STRING(1500),
      DOB: DataTypes.DATE,
      followerNum: DataTypes.INTEGER,
      password: DataTypes.STRING,
      avatarId: DataTypes.STRING,
      backgroundId: DataTypes.STRING,
      wallet: DataTypes.DECIMAL(15, 2),
      isPremium: DataTypes.BOOLEAN,
      status: DataTypes.STRING,
    },
    {
      sequelize,
      modelName: 'User',
      tableName: 'user',
      timestamps: false,
    }
  )

  return User
}
