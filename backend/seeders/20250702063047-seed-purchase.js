/** @type {import('sequelize-cli').Migration} */
import { sequelize } from '../models';

export default {
  async up(queryInterface, Sequelize) {
    const Chapter = sequelize.models.Chapter;
    const Story = sequelize.models.Story;
    const userIds = [6, 8, 9, 10];

    const startDate = new Date('2025-03-01T00:00:00.000Z');
    const endDate = new Date();

    const chapters = await Chapter.findAll({
      attributes: ['chapterId', 'storyId'],
    });
    const stories = await Story.findAll({
      attributes: ['storyId', 'pricePerChapter'],
    });

    const priceMap = {};
    stories.forEach((s) => (priceMap[s.storyId] = s.pricePerChapter));

    const randomDateBetween = (start, end) =>
      new Date(
        start.getTime() + Math.random() * (end.getTime() - start.getTime())
      );

    const purchases = [];
    const reports = {};
    const transactions = [];

    for (let i = 0; i < 100; i++) {
      const { chapterId, storyId } =
        chapters[Math.floor(Math.random() * chapters.length)];
      const price = priceMap[storyId];
      if (!price) continue;

      const userId = userIds[Math.floor(Math.random() * userIds.length)];
      const purchasedAt = randomDateBetween(startDate, endDate);
      const dateStr = purchasedAt.toISOString().slice(0, 10);
      const income = price * 0.2;

      purchases.push({ userId, chapterId, purchasedAt });

      transactions.push({
        userId,
        type: 'purchase',
        money: price,
        status: 'success',
        finishAt: purchasedAt,
      });

      reports[dateStr] = (reports[dateStr] || 0) + income;
    }

    const reportList = Object.entries(reports).map(([date, total]) => ({
      date,
      totalIncome: Math.round(total),
    }));

    await queryInterface.bulkInsert('purchase', purchases);
    await queryInterface.bulkInsert('transaction', transactions);
    await queryInterface.bulkInsert('report', reportList);
  },

  async down(queryInterface, Sequelize) {
    await queryInterface.bulkDelete('purchase', null);
    await queryInterface.bulkDelete('transaction', null);
    await queryInterface.bulkDelete('report', null);
  },
}
