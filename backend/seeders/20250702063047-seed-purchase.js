/** @type {import('sequelize-cli').Migration} */

export default {
  async up(queryInterface, Sequelize) {
    const userIds = [6, 8, 9, 10];
    const purchases = [];

    const randomDateBetween = (start, end) => {
      return new Date(
        start.getTime() + Math.random() * (end.getTime() - start.getTime())
      );
    };

    const startDate = new Date('2025-03-01T00:00:00.000Z');
    const endDate = new Date();

    for (let i = 0; i < 100; i++) {
      const chapterId = Math.floor(Math.random() * 270) + 1;
      const userId = userIds[Math.floor(Math.random() * userIds.length)];
      const purchasedAt = randomDateBetween(startDate, endDate);

      purchases.push({
        userId,
        chapterId,
        purchasedAt,
      });
    }

    await queryInterface.bulkInsert('purchase', purchases, {});
  },

  async down(queryInterface, Sequelize) {
    await queryInterface.bulkDelete('purchase', null, {});
  },
};
