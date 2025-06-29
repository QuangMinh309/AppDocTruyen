import { sequelize } from '../../models/index.js';
import { handleTransaction } from '../../utils/handle_transaction.util.js';
import { formatDate } from '../../utils/date.util.js';
import ApiError from '../../utils/api_error.util.js';
import NotificationService from '../notification.service.js';


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

            await NotificationService.createNotification(
                'TRANSACTION_APPROVAL',
                `Giao dịch gần đây của bạn ${approvalData.status === 'success' ? 'được xác thực' : 'bị từ chối.Vui lòng kiểm tra lại giao dịch đã thực hiện trước đó.'
                }.`,
                0,
                trans.userId,
                transaction
            );


            return {
                result: approvalData.status
            };
        });
    },
};

export default TransactionManagerService;
