import ApiError from '../../utils/api_error.util.js';
import { validateUser } from '../../utils/user.util.js';
import TransactionService from '../transaction.service.js';
import NotificationService from '../notification.service.js';
import { handleTransaction } from '../../utils/handle_transaction.util.js';



const WalletManagementService = {
    async walletChange(userId, data) {
        return await handleTransaction(async (transaction) => {
            const user = await validateUser(userId);

            const currentWallet = parseInt(user.wallet, 10)
            const amountToAdd = (data.type === "withdraw") ? -data.money : data.money;
            // Kiểm tra số dư khi rút tiền
            if (data.type === "withdraw" && currentWallet + amountToAdd < 0) {
                throw new ApiError("Tiền trong ví không đủ để rút!", 400);
            }
            await user.update({
                wallet: currentWallet + amountToAdd,
                isPremium: true,
            });

            await TransactionService.createTransaction({
                userId,
                type: data.type,
                money: data.money,
                status: 'pending'
            });

            await NotificationService.createNotification(
                'TRANSACTION_APPROVAL',
                `Giao dịch ${data.type === 'withdraw' ? 'rút tiền' : 'nạp tiền'} của người dùng ${userId} cần được xác thực, vui lòng kiểm tra tài khoản và thực hiện các thao tác cần thiết.`,
                0,
                userId,
                transaction
            );

            return { message: 'Giao dịch thành công' };
        })
    }

}

export default WalletManagementService;
