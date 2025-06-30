import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class Role extends Model {
    static associate(models) {
      // Role has many Users
      Role.hasMany(models.User, {
        foreignKey: 'roleId',
        as: 'users',
      })

      // Role has many Functionalities through Authorization (many-to-many)
      // Role.belongsToMany(models.Functionality, {
      //   through: models.Authorization,
      //   foreignKey: 'roleId',
      //   otherKey: 'funcId',
      //   as: 'functionalities',
      // })
    }
  }

  Role.init(
    {
      roleId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
      },
      roleName: DataTypes.STRING,
    },
    {
      sequelize,
      modelName: 'Role',
      tableName: 'role',
      timestamps: false,
    }
  )

  return Role
}
