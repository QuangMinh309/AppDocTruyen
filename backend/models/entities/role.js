import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class Role extends Sequelize.Model {}
  Role.init(
    {
      roleId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      roleName: {
        type: DataTypes.STRING,
      },
    },
    {
      sequelize,
      modelName: "Role",
      tableName: "role",
      timestamps: false,
    }
  );

  Role.associate = (models) => {
    Role.hasMany(models.User, { foreignKey: "roleId" });
    Role.hasMany(models.Authorization, { foreignKey: "roleId" });
  };

  return Role;
};
