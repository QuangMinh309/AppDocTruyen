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
      expirateAt: new Date(date.getTime() + 30 * 24 * 60 * 60 * 1000), // +30 ngày
    };
  };

  const userIds = [1, 3, 4, 5, 7, 8];

  const premiums = userIds.map((userId, index) => {
    const { createdAt, expirateAt } = generateRandomDate();
    return {
      premiumId: index + 1,
      userId,
      CreatedAt: createdAt,
      expirateAt,
    };
  });

  await queryInterface.bulkInsert('premium', premiums);
}

export async function down(queryInterface, Sequelize) {
  await queryInterface.bulkDelete('premium', {
    userId: { [Op.in]: [3, 4, 5, 7] },
  });
}
