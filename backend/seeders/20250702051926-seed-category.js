export async function up(queryInterface, Sequelize) {
  await queryInterface.bulkInsert('category', [
    { categoryId: 1, categoryName: 'Tiên Hiệp' },
    { categoryId: 2, categoryName: 'Huyền Huyễn' },
    { categoryId: 3, categoryName: 'Dị Giới' },
    { categoryId: 4, categoryName: 'Xuyên Không' },
    { categoryId: 5, categoryName: 'Kiếm Hiệp' },
    { categoryId: 6, categoryName: 'Ngôn Tình' },
    { categoryId: 7, categoryName: 'Ngược' },
    { categoryId: 8, categoryName: 'Sủng' },
    { categoryId: 9, categoryName: 'Trọng Sinh' },
    { categoryId: 10, categoryName: 'Bách Hợp' },
    { categoryId: 11, categoryName: 'Linh Dị' },
    { categoryId: 12, categoryName: 'Đô Thị' },
  ]);
}

export async function down(queryInterface, Sequelize) {
  await queryInterface.bulkDelete('category', null, {});
}
