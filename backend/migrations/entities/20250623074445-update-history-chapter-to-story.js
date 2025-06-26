'use strict';
export default {
  async up(queryInterface, Sequelize) {
    // 1. Xóa ràng buộc khóa ngoại chapterId nếu có
    await queryInterface.removeConstraint('history', 'history_chapterId_fkey').catch(() => { }); // nếu không tồn tại thì bỏ qua

    // 2. Xóa cột chapterId
    await queryInterface.removeColumn('history', 'chapterId');

    // 3. Thêm cột storyId
    await queryInterface.addColumn('history', 'storyId', {
      type: Sequelize.INTEGER,
      allowNull: true, // cho phép null nếu cần
      references: {
        model: 'story', // tên bảng liên kết
        key: 'storyId'  // khoá chính trong bảng story
      },
      onUpdate: 'CASCADE',
      onDelete: 'SET NULL'
    });
  },

  async down(queryInterface, Sequelize) {
    // Đảo ngược: xóa storyId, thêm lại chapterId
    await queryInterface.removeConstraint('history', 'history_storyId_fkey').catch(() => { });
    await queryInterface.removeColumn('history', 'storyId');

    await queryInterface.addColumn('history', 'chapterId', {
      type: Sequelize.INTEGER,
      allowNull: true,
      references: {
        model: 'chapter',
        key: 'chapterId'
      },
      onUpdate: 'CASCADE',
      onDelete: 'SET NULL'
    });
  }
};
