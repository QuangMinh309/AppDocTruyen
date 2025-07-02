/** @type {import('sequelize-cli').Seed} */
export default {
  async up(queryInterface, Sequelize) {
    const readList = [];

    const totalNameLists = 5;
    const totalStories = 60;

    for (let nameListId = 1; nameListId <= totalNameLists; nameListId++) {
      // Random 10 unique storyId cho mỗi nameList
      const storyIds = new Set();
      while (storyIds.size < 10) {
        const storyId = Math.floor(Math.random() * totalStories) + 1;
        storyIds.add(storyId);
      }

      for (const storyId of storyIds) {
        readList.push({
          nameListId,
          storyId,
        });
      }
    }

    await queryInterface.bulkInsert('read_list', readList, {});
  },

  async down(queryInterface, Sequelize) {
    await queryInterface.bulkDelete('read_list', null, {});
  },
};
