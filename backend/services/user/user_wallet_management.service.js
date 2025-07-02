import ApiError from '../../utils/api_error.util.js';
import { validateUser } from '../../utils/user.util.js';
import TransactionService from '../transaction.service.js';
import NotificationService from '../notification.service.js';
import { handleTransaction } from '../../utils/handle_transaction.util.js';
import { sequelize } from '../../models/index.js'

const User = sequelize.models.User


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
            if (user.roleId != 1) {
                const admins = await User.findAll({
                    where: { roleId: 1 }
                })

                if (!admins.length) throw new ApiError("ứng dụng không tồn tại admin để tiến hành kiểm duyệt", 404)

                await Promise.all(
                    admins.map(async (admin) => {
                        await NotificationService.createNotification(
                            'TRANSACTION_PENDING_APPROVAL',
                            (data.type == 'deposit') ?
                                `Giao dịch nạp tiền của người dùng ${userId} cần được xác thực, vui lòng kiểm tra tài khoản và thực hiện các thao tác cần thiết.`
                                : `Người dùng ${user.userId} yêu cầu rút tiền về tài khoản: ${data.bankAccountData.accountNumber}-${data.bankAccountData.accountHolder}-${data.bankAccountData.bankName}`,
                            0,
                            admin.userId,
                            transaction
                        );
                    })
                );
            }


            await TransactionService.createTransaction({
                userId,
                type: data.type,
                money: data.money,
                status: (user.roleId == 1) ? 'success' : 'pending'
            });

            return { message: (user.roleId === 1) ? "Giao dịch thành công" : 'Giao dịch hoàn tất.Ví của bạn sẽ được cập nhật khi admin kiểm duyệt thành công.' };
        })
    }

}

export default WalletManagementService;
