import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class Premium extends Model {
    static associate(models) {
      // Premium belongs to User
      Premium.belongsTo(models.User, {
        foreignKey: 'userId',
        as: 'user',
      })
    }
  }

  Premium.init(
    {
      premiumId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
      },
      userId: DataTypes.INTEGER,
      CreatedAt: DataTypes.DATE,
      expirateAt: {
        type: DataTypes.DATE,
        allowNull: true
      }
    },
    {
      sequelize,
      modelName: 'Premium',
      tableName: 'premium',
      timestamps: false,
    }
  )

  return Premium
}
