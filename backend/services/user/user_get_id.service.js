import { sequelize } from '../../models/index.js'
import ApiError from '../../utils/api_error.util.js'
import { validateUser } from '../../utils/user.util.js'

const User = sequelize.models.User
const Role = sequelize.models.Role
const Story = sequelize.models.Story
const ReadList = sequelize.models.ReadList

const getUserById = async (userId) => {
  try {
    const user = await User.findByPk(userId, {
      attributes: [
        'userId',
        'mail',
        'userName',
        'dUserName',
        'DOB',
        'about',
        'followerNum',
      ],
      include: [
        { model: Role, as: 'role', attributes: ['roleId', 'roleName'] },
        {
          model: Story,
          as: 'stories',
          attributes: ['storyId', 'storyName', 'price'],
          include: [
            {
              model: User,
              as: 'author',
              attributes: ['userName'],
            },
            {
              model: Vote,
              as: 'votes',
              attributes: [],
              through: { attributes: [] },
            },
          ],
        },
        {
          model: ReadList,
          as: 'readLists',
          attributes: ['description'],
          include: [
            {
              model: Story,
              as: 'story',
              attributes: ['storyId', 'storyName', 'coverImgId'],
              limit: 3,
            },
          ],
        },
      ],
    })

    validateUser(user.userId)

    const storyCount = await Story.count({ where: { userId } })
    const readListCount = await ReadList.count({ where: { userId } })

    const userData = user.toJSON()
    userData.storyCount = storyCount
    userData.readListCount = readListCount

    userData.stories = userData.stories.map((story) => ({
      ...story,
      voteCount: story.votes.length,
    }))

    return userData
  } catch (err) {
    if (err instanceof ApiError) throw err
    console.error('Error in getUserById:', err)
    throw new ApiError('Lỗi khi lấy thông tin người dùng', 500)
  }
}

export default getUserById
