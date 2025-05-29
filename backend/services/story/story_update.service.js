import { sequelize } from '../../models/index.js'
import { validateStory } from '../../utils/story.util.js'
import { handleTransaction } from '../../utils/transaction.util.js'
import { uploadImageToCloudinary } from '../cloudinary.service.js'

const StoryCategory = sequelize.models.StoryCategory

const updateStory = async (storyId, storyData, userId) => {
  return await handleTransaction(async (transaction) => {
    const story = await validateStory(storyId, userId)

    let coverImgId = story.coverImgId
    if (storyData.coverImg) {
      const uploadResult = await uploadImageToCloudinary(
        storyData.coverImg,
        'stories/covers'
      )
      coverImgId = uploadResult.public_id
    }

    await story.update(
      {
        ...storyData,
        coverImgId,
      },
      { transaction }
    )

    if (storyData.categories && Array.isArray(storyData.categories)) {
      await StoryCategory.destroy({ where: { storyId }, transaction })
      if (storyData.categories.length > 0) {
        const categoryAssociations = storyData.categories.map((categoryId) => ({
          storyId,
          categoryId,
        }))
        await StoryCategory.bulkCreate(categoryAssociations, {
          transaction,
        })
      }
    }

    return story
  })
}

export default updateStory
