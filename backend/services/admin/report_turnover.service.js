import { Op } from 'sequelize';
import { formatDate } from '../../utils/date.util';

const Report = sequelize.models.Report;

const ReportService = {
  async updateDailyRevenue(sequelize, transaction, options = {}) {
    const { time, money, type, status } = transaction;

    // Chỉ cập nhật nếu giao dịch hoàn tất và là premium hoặc purchase
    if (
      status !== 'success' ||
      !time ||
      !['premium', 'purchase'].includes(type)
    )
      return;

    const date = formatDate(time);
    const { DailyRevenue } = sequelize.models;

    let dailyRevenue = await DailyRevenue.findOne({
      where: { date },
      ...options,
    });
    if (!dailyRevenue) {
      dailyRevenue = await DailyRevenue.create(
        { date, totalIncome: 0, totalExpense: 0, netRevenue: 0 },
        options
      );
    }

    // Cập nhật totalIncome (premium và purchase đều là thu)
    const updateData = {
      totalIncome: dailyRevenue.totalIncome + money,
    };

    // Cập nhật netRevenue (vì không có chi trong trường hợp này)
    updateData.netRevenue = updateData.totalIncome - dailyRevenue.totalExpense;

    // Cập nhật bản ghi
    await dailyRevenue.update(updateData, options);
  },

  async getDailyRevenueByMonth(sequelize, year, month) {
    // Định dạng tháng (YYYY-MM)
    const monthPattern = `${year}-${month.toString().padStart(2, '0')}%`;

    // Truy vấn tất cả bản ghi trong tháng
    const dailyReport = await Report.findAll({
      where: {
        date: {
          [Op.like]: monthPattern,
        },
      },
      order: [['date', 'ASC']],
    });

    // Tạo danh sách tất cả ngày trong tháng
    const daysInMonth = new Date(year, month, 0).getDate();
    const result = Array.from({ length: daysInMonth }, (_, i) => {
      const date = `${year}-${month.toString().padStart(2, '0')}-${(i + 1)
        .toString()
        .padStart(2, '0')}`;
      const record = dailyReport.find((r) => r.date === date) || {
        date,
        totalIncome: 0,
        totalExpense: 0,
        netRevenue: 0,
      };
      return record;
    });

    return result;
  },
};

export default ReportService;
