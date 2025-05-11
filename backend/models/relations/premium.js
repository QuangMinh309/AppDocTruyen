import { Sequelize, DataTypes } from "sequelize";

export default (sequelize) => {
  class Premium extends Sequelize.Model {}
  Premium.init(
    {
      premiumId: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: DataTypes.INTEGER,
      },
      userId: {
        type: DataTypes.INTEGER,
      },
      CreatedAt: {
        type: DataTypes.DATE,
      },
    },
    {
      sequelize,
      modelName: "Premium",
      tableName: "premium",
      timestamps: false,
    }
  );

  Premium.associate = (models) => {
    Premium.belongsTo(models.User, { foreignKey: "userId" });
  };

  return Premium;
};
