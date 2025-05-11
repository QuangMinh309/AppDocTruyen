import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class ReadList extends Sequelize.Model {}
  ReadList.init(
    {
      storyId: {
        allowNull: false,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      nameListId: {
        allowNull: false,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
    },
    {
      sequelize,
      modelName: "ReadList",
      tableName: "read_list",
      timestamps: false,
    }
  );

  ReadList.associate = (models) => {
    ReadList.belongsTo(models.Story, { foreignKey: "storyId" });
    ReadList.belongsTo(models.NameList, { foreignKey: "nameListId" });
  };

  return ReadList;
};
