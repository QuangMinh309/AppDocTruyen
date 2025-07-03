import { Op } from 'sequelize';

/** @type {import('sequelize-cli').Migration} */
export async function up(queryInterface, Sequelize) {
  const now = new Date();
  const march2025 = new Date('2025-03-01');

  const generateRandomDate = () => {
    const start = march2025.getTime();
    const end = now.getTime();
    const date = new Date(start + Math.random() * (end - start));
    return {
      createdAt: date,
      expirateAt: new Date(date.getTime() + 30 * 24 * 60 * 60 * 1000),
    };
  };

  const userIds = [3, 4, 5, 7];
  const premiums = [];
  const transactions = [];
  const reportMap = {};

  userIds.forEach((userId, index) => {
    const { createdAt, expirateAt } = generateRandomDate();
    const date = createdAt.toISOString().slice(0, 10); // YYYY-MM-DD

    premiums.push({
      premiumId: index + 1,
      userId,
      CreatedAt: createdAt,
      expirateAt,
    });

    transactions.push({
      transactionId: index + 1,
      userId,
      type: 'premium',
      money: 50000,
      status: 'success',
      finishAt: createdAt,
    });

    // cộng vào report
    reportMap[date] = (reportMap[date] || 0) + 50000;
  });

  const reports = Object.entries(reportMap).map(([date, totalIncome]) => ({
    date,
    totalIncome,
  }));

  await queryInterface.bulkInsert('premium', premiums);
  await queryInterface.bulkInsert('transaction', transactions);
  await queryInterface.bulkInsert('report', reports);
}

export async function down(queryInterface, Sequelize) {
  await queryInterface.bulkDelete('premium', {
    userId: { [Op.in]: [3, 4, 5, 7] },
  });

  await queryInterface.bulkDelete('transaction', {
    userId: { [Op.in]: [3, 4, 5, 7] },
    type: 'premium',
  });

  await queryInterface.bulkDelete('report', null);
}
