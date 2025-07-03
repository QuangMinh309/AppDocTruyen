import { sequelize } from '../../models/index.js';
import { handleTransaction } from '../../utils/handle_transaction.util.js';
import { validateUser } from '../../utils/user.util.js';
import ApiError from '../../utils/api_error.util.js';
import NotificationService from '../notification.service.js';
import { notifyUser } from '../../utils/notify_user.util.js';


const Transaction = sequelize.models.Transaction


const TransactionManagerService = {
    async approveTransaction(transactionId, approvalData) {
        return await handleTransaction(async (transaction) => {
            const trans = await Transaction.findByPk(transactionId, { transaction });
            if (!trans) {
                throw new ApiError('Giao dịch không tồn tại tồn tại', 404);
            }
            if (trans.status !== 'pending') {
                throw new ApiError('Giao dịch đã được duyệt', 400);
            }

            await trans.update(
                {
                    status: approvalData.status,
                    finishAt: new Date(),
                },
                { transaction }
            );

            const user = await validateUser(trans.userId);
            const currentWallet = user.wallet
            user.update({
                wallet: currentWallet + ((trans.type === "withdraw") ? -trans.money : trans.money)
            })

            await NotificationService.createNotification(
                'TRANSACTION_APPROVAL',
                `Giao dịch ${trans.type === 'withdraw' ? "rút" : "nạp"} ${trans.money}đ gần đây của bạn ${approvalData.status === 'success' ? 'được xác thực' : 'bị từ chối.Vui lòng kiểm tra lại giao dịch đã thực hiện trước đó.'
                }.`,
                0,
                trans.userId,
                transaction
            );
            notifyUser(trans.userId)
            // console.log('notifiUsser')
            

            return {
                result: approvalData.status
            };
        });
    },
};

export default TransactionManagerService;
