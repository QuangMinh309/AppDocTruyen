export default {
  async up(queryInterface, Sequelize) {
    await queryInterface.bulkInsert('name_list', [
      {
        nameListId: 1,
        nameList: 'Bộ sưu tập số 1',
        userId: 3,
        description: 'Danh sách đọc được tạo ngẫu nhiên.',
      },
      {
        nameListId: 2,
        nameList: 'Danh sách đặc biệt',
        userId: 7,
        description: 'Tổng hợp một số truyện nổi bật.',
      },
      {
        nameListId: 3,
        nameList: 'Tủ sách cá nhân',
        userId: 1,
        description: 'Lưu trữ truyện riêng của tôi.',
      },
      {
        nameListId: 4,
        nameList: 'Bộ chọn lọc',
        userId: 5,
        description: 'Chọn lọc một số đầu truyện hay.',
      },
      {
        nameListId: 5,
        nameList: 'Lưu tạm',
        userId: 5,
        description: 'Tạm lưu những truyện quan tâm.',
      },
      {
        nameListId: 6,
        nameList: 'Ghi chú truyện',
        userId: 3,
        description: 'Danh sách dùng để ghi chú vài truyện.',
      },
      {
        nameListId: 7,
        nameList: 'Bộ cũ',
        userId: 7,
        description: 'Tập hợp vài truyện từng đọc.',
      },
      {
        nameListId: 8,
        nameList: 'Danh sách ngẫu nhiên',
        userId: 9,
        description: 'Một danh sách ngẫu nhiên được tạo.',
      },
    ]);
  },

  async down(queryInterface, Sequelize) {
    await queryInterface.bulkDelete('name_list', null, {});
  },
};
