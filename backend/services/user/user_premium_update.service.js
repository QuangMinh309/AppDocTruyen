
import { sequelize } from '../../models/index.js';
import ApiError from '../../utils/api_error.util.js';
import { validateUser } from '../../utils/user.util.js';


const Premium = sequelize.models.Premium

const userPremiumService = {
    UpdateUserPremiumInfo: async (userId) => {
        try {
            const user = await validateUser(userId);

            const premium = await Premium.findOne({
                where: { userId: yourUserId },
                order: [['createdAt', 'DESC']]
            });

            if (!premium) throw new ApiError("người dùng này chưa đăng kí Premium")
            if (premium.expirateAt && new Date(premium.expirateAt) < new Date()) {
                await user.update({
                    isPremium: false,
                });
            }

            return true
        } catch (err) {
            if (err instanceof ApiError) throw err;
            throw new ApiError('Lỗi khi mua premium', 500);
        }
    },
};

export default userPremiumService;
