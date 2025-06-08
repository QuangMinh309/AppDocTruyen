import { Model } from 'sequelize';

export default (sequelize, DataTypes) => {
  class PasswordReset extends Model {
    static associate(models) {
      // PasswordReset belongs to User
      PasswordReset.belongsTo(models.User, {
        foreignKey: 'userId',
        as: 'user',
      });
    }
  }

  PasswordReset.init(
    {
      OTP: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
      },
      userId: DataTypes.INTEGER,
      isUsed: DataTypes.BOOLEAN,
      createdAt: {
        type: DataTypes.DATE,
        allowNull: false,
      },
    },
    {
      sequelize,
      modelName: 'PasswordReset',
      tableName: 'password_reset',
      timestamps: true, // Bật timestamps để Sequelize tự động quản lý createdAt
      updatedAt: false,
    }
  );

  return PasswordReset;
};