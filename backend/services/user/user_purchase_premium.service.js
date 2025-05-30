import { sequelize } from '../../models/index.js'
import ApiError from '../../utils/api_error.util.js'
import { validateUser } from '../../utils/user.util.js'

const Parameter = sequelize.models.Parameter
const Transaction = sequelize.models.Transaction

const purchasePremium = async (userId) => {
  try {
    const user = await validateUser(userId)

    if (user.isPremium) {
      throw new ApiError('Bạn đã là thành viên premium', 400)
    }

    const parameters = await Parameter.findOne()
    if (!parameters) {
      throw new ApiError('Không tìm thấy tham số hệ thống', 500)
    }

    const premiumCost = 100 // Chi phí mua premium
    if (user.wallet < premiumCost) {
      throw new ApiError('Số dư ví không đủ', 400)
    }

    const premiumExpiresAt = new Date(
      Date.now() + parameters.Premium_Period * 24 * 60 * 60 * 1000
    )

    await user.update({
      wallet: user.wallet - premiumCost,
      isPremium: true,
      premiumExpiresAt,
    })

    await Transaction.create({
      userId,
      money: -premiumCost,
      status: 'completed',
      createdAt: new Date(),
    })

    return { message: 'Mua premium thành công' }
  } catch (err) {
    if (err instanceof ApiError) throw err
    throw new ApiError('Lỗi khi mua premium', 500)
  }
}

export default purchasePremium
