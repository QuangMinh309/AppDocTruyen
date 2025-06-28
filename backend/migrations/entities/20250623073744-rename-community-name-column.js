'use strict';


export default {
  async up(queryInterface, Sequelize) {
    await queryInterface.renameColumn('community', 'communitytName', 'communityName');
    await queryInterface.renameColumn('community', 'menberNum', 'memberNum');
  },

  async down(queryInterface, Sequelize) {
    await queryInterface.renameColumn('community', 'communityName', 'communitytName');
    Interface.renameColumn('community', 'menberNum', 'memberNum');
  }
};
