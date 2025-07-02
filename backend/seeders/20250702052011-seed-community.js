export async function up(queryInterface, Sequelize) {
  await queryInterface.bulkInsert('community', [
    {
      communityName: 'Thiên Hạ Hội',
      categoryId: 1,
      memberNum: 0,
      avatarId: null,
      description: 'Cộng đồng dành cho người yêu thích phiêu lưu và tu tiên.',
    },
    {
      communityName: 'Huyễn Ảo Đường',
      categoryId: 2,
      memberNum: 0,
      avatarId: null,
      description: 'Nơi chia sẻ những câu chuyện kỳ ảo, hấp dẫn.',
    },
    {
      communityName: 'Thế Giới Dị Biệt',
      categoryId: 3,
      memberNum: 0,
      avatarId: null,
      description: 'Thảo luận các truyện dị giới độc đáo.',
    },
    {
      communityName: 'Xuyên Không Vương Quốc',
      categoryId: 4,
      memberNum: 0,
      avatarId: null,
      description: 'Tụ hội fan hâm mộ truyện xuyên không.',
    },
    {
      communityName: 'Kiếm Đạo Tông',
      categoryId: 5,
      memberNum: 0,
      avatarId: null,
      description: 'Nơi hội ngộ những người yêu thích kiếm hiệp.',
    },
    {
      communityName: 'Ngôn Tình Quán',
      categoryId: 6,
      memberNum: 0,
      avatarId: null,
      description: 'Tâm sự, bàn luận các truyện tình cảm hấp dẫn.',
    },
    {
      communityName: 'Hội Ngược Tâm',
      categoryId: 7,
      memberNum: 0,
      avatarId: null,
      description: 'Cộng đồng yêu thích thể loại đau thương, day dứt.',
    },
    {
      communityName: 'Sủng Ngọt Café',
      categoryId: 8,
      memberNum: 0,
      avatarId: null,
      description: 'Nơi để thư giãn với những truyện sủng ngọt ngào.',
    },
  ]);
}

export async function down(queryInterface, Sequelize) {
  await queryInterface.bulkDelete('community', null, {});
}
