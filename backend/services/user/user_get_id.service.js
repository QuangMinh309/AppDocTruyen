import { sequelize } from '../../models/index.js'
import ApiError from '../../utils/api_error.util.js'
import { validateUser } from '../../utils/user.util.js'

const User = sequelize.models.User
const Role = sequelize.models.Role
const Story = sequelize.models.Story
const NameList = sequelize.models.NameList

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
        "wallet",
        'avatarId',
        'isPremium',
        'backgroundId',
      ],
      include: [
        { model: Role, as: 'role', attributes: ['roleId', 'roleName'] },
        {
          model: Story,
          as: 'stories',
          attributes: ['storyId', 'storyName', 'voteNum'],
          include: [
            {
              model: User,
              as: 'author',
              attributes: ['userName'],
            },
          ],
        },
        {
          model: NameList,
          as: 'nameLists',
          attributes: ['description'],
          include: [
            {
              model: Story,
              as: 'stories',
              attributes: ['storyId', 'storyName', 'coverImgId'],
            },
          ],
        },
      ],
    })

    validateUser(user.userId)

    const storyCount = await Story.count({ where: { userId } })
    const nameListCount = await NameList.count({ where: { userId } })

    const { DOB, ...rest } = user.toJSON()
    const formattedDOB = DOB ? new Date(DOB).toISOString().split('T')[0] : null

    const userData = {
      ...rest,
      DOB: formattedDOB,
    }

    userData.storyCount = storyCount
    userData.nameListCount = nameListCount

    return userData
  } catch (err) {
    if (err instanceof ApiError) throw err
    console.error('Error in getUserById:', err)
    throw new ApiError('Lỗi khi lấy thông tin người dùng', 500)
  }
}

export default getUserById
