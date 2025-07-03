
import { sequelize } from '../../models/index.js';
import ApiError from '../../utils/api_error.util.js';
import { validateUser } from '../../utils/user.util.js';
import TransactionService from '../transaction.service.js';
import ChapterService from '../chapter/chapter.service.js';
import getStoryById from '../story/story_get_id.service.js';
import ReportService from '../admin/report_turnover.service.js';

const Parameter = sequelize.models.Parameter;
const Purchase = sequelize.models.Purchase
const User = sequelize.models.User


const PurchaseChapterService = {
    async purchaseChapter(userId, chapterId) {
        try {
            const user = await validateUser(userId);

            const parameters = await Parameter.findOne();
            if (!parameters) {
                throw new ApiError('Không tìm thấy tham số hệ thống', 500);
            }

            const chapter = await ChapterService.getChapterById(chapterId)
            if (!chapter) throw new ApiError("chapter không tồn tại!", 404)

            const story = await getStoryById(chapter.storyId)


            if (user.wallet < story.pricePerChapter) {
                throw new ApiError('Số dư ví không đủ', 400);
            }

            const purchase = await Purchase.findOne({
                where: { chapterId, userId },
                order: [['purchasedAt', 'DESC']],
                attributes: ['purchasedId', 'chapterId', 'purchasedAt']
            });
            
            // console.log(purchase)


            if (purchase) {

                const expirateAt = new Date(
                    purchase.purchasedAt.getTime() + parameters.Chapter_Access_Duration * 24 * 60 * 60 * 1000
                );
                if (expirateAt > new Date())
                    throw new ApiError('Bạn đã mua chapter này rồi!', 400);
            }

            await user.update({
                wallet: user.wallet - story.pricePerChapter,
            });

            const author = await User.findByPk(story.userId)
            await author.update({ wallet: user.wallet + story.pricePerChapter*0.8 })

            await Purchase.create({ userId, chapterId, purchasedAt: new Date() })

            await TransactionService.createTransaction({
                userId,
                type: "purchase",
                money: story.pricePerChapter,
                status: 'success',
                finishAt: new Date(),
            });

            await ReportService.updateDailyRevenue(sequelize, {
                userId,
                type: "purchase",
                money: story.pricePerChapter * 0.2,
                status: 'success',
                finishAt: new Date(),
            });

            return { message: 'Mua chapter thành công' };
        } catch (err) {
            // console.log(err)
            if (err instanceof ApiError) throw err;
            throw new ApiError('Lỗi khi mua chapter', 500);
        }
    }

}

export default PurchaseChapterService;
