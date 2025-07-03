import { Op } from 'sequelize';
import { formatDate } from '../../utils/date.util.js';
import ApiError from '../../utils/api_error.util.js';

const ReportService = {
  async updateDailyRevenue(sequelize, transaction, options = {}) {
    const { finishAt: time, money, type, status } = transaction;

    // Chỉ cập nhật nếu giao dịch hoàn tất và là premium hoặc purchase
    if (
      status !== 'success' ||
      !time ||
      !['premium', 'purchase'].includes(type)
    ) {
      return;
    }

    const date = formatDate(time);
    const { Report } = sequelize.models;

    let report = await Report.findOne({ where: { date }, ...options });
    if (!report) {
      report = await Report.create({ date, totalIncome: 0 }, options);
    }

    // Cập nhật totalIncome
    await report.update({ totalIncome: report.totalIncome + money }, options);
  },

  async getDailyRevenueByMonth(sequelize, year, month) {
    try {
      const { Report } = sequelize.models;
      const monthPattern = `${year}-${month.toString().padStart(2, '0')}%`;

      const dailyReports = await Report.findAll({
        where: {
          date: { [Op.like]: monthPattern },
        },
        order: [['date', 'ASC']],
      });

      const daysInMonth = new Date(year, month, 0).getDate();
      const result = Array.from({ length: daysInMonth }, (_, i) => {
        const date = `${year}-${month.toString().padStart(2, '0')}-${(i + 1)
          .toString()
          .padStart(2, '0')}`;
        const record = dailyReports.find((r) => r.date === date) || {
          date,
          totalIncome: 0,
        };
        return {
          date: record.date,
          totalIncome: record.totalIncome,
        };
      });

      return result;
    } catch (err) {
      throw new ApiError('Lỗi khi lấy báo cáo doanh thu', 500);
    }
  },
};

export default ReportService;
