import { sequelize } from '../../models/index.js';
import bcrypt from 'bcrypt';
import ApiError from '../../utils/api_error.util.js';
import { validateUser } from '../../utils/user.util.js';
import { formatDate } from '../../utils/date.util.js';
import { handleTransaction } from '../../utils/handle_transaction.util.js';
import {
  uploadImageToCloudinary,
  deleteImageOnCloudinary,
} from '../cloudinary.service.js';

const User = sequelize.models.User;
const Role = sequelize.models.Role;

const updateUser = async (userId, data, avatarFile, backgroundFile) => {
  return await handleTransaction(async (transaction) => {
    try {
      const user = await validateUser(userId);

      const { password, ...updateData } = data;

      if (updateData.mail && updateData.mail !== user.mail) {
        const existingUser = await User.findOne({
          where: { mail: updateData.mail },
          transaction,
        });
        if (existingUser) {
          throw new ApiError('Email đã được sử dụng bởi người dùng khác', 400);
        }
      }

      let avatarId = user.avatarId;
      let backgroundId = user.backgroundId;

      if (avatarFile && avatarFile.buffer) {
        try {
          if (avatarId) {
            try {
              await deleteImageOnCloudinary(avatarId);
            } catch (error) {
              throw new ApiError('Xóa ảnh đại diện cũ thất bại', 500);
            }
          }

          const uploadResult = await uploadImageToCloudinary(
            avatarFile.buffer,
            'user_avatars'
          );
          avatarId = uploadResult.public_id;
        } catch (error) {
          throw new ApiError('Tải ảnh đại diện lên thất bại', 500);
        }
      }

      if (backgroundFile && backgroundFile.buffer) {
        try {
          if (backgroundId) {
            try {
              await deleteImageOnCloudinary(backgroundId);
            } catch (error) {
              throw new ApiError('Xóa ảnh nền cũ thất bại', 500);
            }
          }

          const uploadResult = await uploadImageToCloudinary(
            backgroundFile.buffer,
            'user_backgrounds'
          );
          backgroundId = uploadResult.public_id;
        } catch (error) {
          throw new ApiError('Tải ảnh nền lên thất bại', 500);
        }
      }

      if (password) {
        updateData.password = await bcrypt.hash(password, 10);
      }

      await user.update(
        {
          ...updateData,
          avatarId,
          backgroundId,
        },
        { transaction }
      );

      const updatedUser = await User.findByPk(userId, {
        attributes: { exclude: ['password'] },
        include: [
          { model: Role, as: 'role', attributes: ['roleId', 'roleName'] },
        ],
        transaction,
      });

      const { DOB, ...rest } = updatedUser.toJSON();
      const formattedDOB = DOB ? formatDate(DOB) : null;

      return {
        ...rest,
        DOB: formattedDOB,
      };
    } catch (err) {
      if (err instanceof ApiError) throw err;
      throw new ApiError('Lỗi khi cập nhật người dùng', 500);
    }
  });
};

export default updateUser;
