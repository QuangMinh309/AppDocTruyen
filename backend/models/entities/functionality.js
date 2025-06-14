import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class Functionality extends Model {
    static associate(models) {
      // Functionality belongs to many Roles (many-to-many)
      Functionality.belongsToMany(models.Role, {
        through: models.Authorization,
        foreignKey: 'funcId',
        otherKey: 'roleId',
        as: 'roles',
      })
    }
  }

  Functionality.init(
    {
      funcId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
      },
      funcName: DataTypes.STRING,
      funcViewId: DataTypes.STRING,
    },
    {
      sequelize,
      modelName: 'Functionality',
      tableName: 'functionality',
      timestamps: false,
    }
  )

  return Functionality
}
