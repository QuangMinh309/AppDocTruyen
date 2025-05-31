import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class Authorization extends Model {
    static associate(models) {
      // Junction table associations are defined in the related models
    }
  }

  Authorization.init(
    {
      roleId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        allowNull: false,
      },
      funcId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        allowNull: false,
      },
    },
    {
      sequelize,
      modelName: 'Authorization',
      tableName: 'authorization',
      timestamps: false,
    }
  )

  return Authorization
}
