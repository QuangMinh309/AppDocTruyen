import { sequelize } from '../../models/index.js';
import ApiError from '../../utils/api_error.util.js';
import { validateUser } from '../../utils/user.util.js';
import TransactionService from '../transaction.service.js';
import ReportService from '../admin/report_turnover.service.js';

const Parameter = sequelize.models.Parameter;
const Premium = sequelize.models.Premium;

const purchasePremium = async (userId) => {
  try {
    const user = await validateUser(userId);

    if (user.isPremium) {
      throw new ApiError('Bạn đã là thành viên premium', 400);
    }

    const parameters = await Parameter.findOne();
    if (!parameters) {
      throw new ApiError('Không tìm thấy tham số hệ thống', 500);
    }

    const premiumCost = 50000;
    if (user.wallet < premiumCost) {
      throw new ApiError('Số dư ví không đủ', 400);
    }

    const expirateAt = new Date(
      Date.now() + parameters.Premium_Period * 24 * 60 * 60 * 1000
    );

    await user.update({
      wallet: user.wallet - premiumCost,
      isPremium: true,
    });

    await Premium.create({
      userId,
      expirateAt,
      CreatedAt: new Date(),
    });

    const transaction = await TransactionService.createTransaction({
      userId,
      type: 'premium',
      money: premiumCost,
      status: 'success',
      finishAt: new Date(),
    });

    await ReportService.updateDailyRevenue(sequelize, transaction);

    return { message: 'Mua premium thành công' };
  } catch (err) {
    // console.log(err);
    if (err instanceof ApiError) throw err;
    throw new ApiError('Lỗi khi mua premium', 500);
  }
};

export default purchasePremium;
