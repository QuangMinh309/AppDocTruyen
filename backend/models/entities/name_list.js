import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class NameList extends Sequelize.Model {}
  NameList.init(
    {
      nameListId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      nameList: {
        type: DataTypes.STRING(255),
      },
      userId: {
        type: DataTypes.INTEGER,
      },
      description: {
        type: DataTypes.STRING(1500),
      },
    },
    {
      sequelize,
      modelName: "NameList",
      tableName: "name_list",
      timestamps: false,
    }
  );

  NameList.associate = (models) => {
    NameList.belongsTo(models.User, { foreignKey: "userId" });
    NameList.hasMany(models.ReadList, { foreignKey: "nameListId" });
  };

  return NameList;
};
