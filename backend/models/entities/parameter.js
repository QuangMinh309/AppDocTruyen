import { Model } from 'sequelize'

export default (sequelize, DataTypes) => {
  class Parameter extends Model {}

  Parameter.init(
    {
      id: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
      },
      Chapter_Access_Duration: {
        type: DataTypes.INTEGER,
        allowNull: false,
        comment: 'Thời hạn truy cập chương (tính bằng ngày)',
      },
      Story_Access_Duration: {
        type: DataTypes.INTEGER,
        allowNull: false,
        comment: 'Thời hạn truy cập truyện (tính bằng ngày)',
      },
      Max_MemberNum: {
        type: DataTypes.INTEGER,
        allowNull: false,
        comment: 'Số lượng thành viên tối đa',
      },
      OTP_Valid_Period: {
        type: DataTypes.INTEGER,
        allowNull: false,
        comment: 'Thời hạn hiệu lực của OTP (tính bằng phút)',
      },
      Premium_Period: {
        type: DataTypes.INTEGER,
        allowNull: false,
        comment: 'Thời hạn premium (tính bằng ngày)',
      },
    },
    {
      sequelize,
      modelName: 'Parameter',
      tableName: 'parameter',
      timestamps: false,
    }
  )

  return Parameter
}
