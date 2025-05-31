import { sequelize } from '../../models/index.js'
import { handleTransaction } from '../../utils/transaction.util.js'
import { uploadImageToCloudinary } from '../cloudinary.service.js'

const Story = sequelize.models.Story
const StoryCategory = sequelize.models.StoryCategory

const createStory = async (storyData, userId) => {
  return await handleTransaction(async (transaction) => {
    let coverImgId = null
    if (storyData.coverImg) {
      const uploadResult = await uploadImageToCloudinary(storyData.coverImg)
      coverImgId = uploadResult.public_id
    }

    const story = await Story.create(
      {
        ...storyData,
        userId,
        status: 'update',
        viewNum: 0,
        voteNum: 0,
        chapterNum: 0,
        coverImgId,
      },
      { transaction }
    )

    if (storyData.categories && storyData.categories.length > 0) {
      const categoryAssociations = storyData.categories.map((categoryId) => ({
        storyId: story.storyId,
        categoryId,
      }))
      await StoryCategory.bulkCreate(categoryAssociations, {
        transaction,
      })
    }

    return story
  })
}

export default createStory
