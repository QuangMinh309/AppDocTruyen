import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class Purchase extends Model {
    static associate(models) {
      // Purchase belongs to User
      Purchase.belongsTo(models.User, {
        foreignKey: 'userId',
        as: 'user',
      })

      // Purchase belongs to Chapter
      Purchase.belongsTo(models.Chapter, {
        foreignKey: 'chapterId',
        as: 'chapter',
      })
    }
  }

  Purchase.init(
    {
      purchasedId: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
      },
      userId: DataTypes.INTEGER,
      chapterId: DataTypes.INTEGER,
      purchasedAt: DataTypes.DATE,
    },
    {
      sequelize,
      modelName: 'Purchase',
      tableName: 'purchase',
      timestamps: false,
    }
  )

  return Purchase
}
