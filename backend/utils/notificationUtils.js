import db from "../models/index.js";
import ApiError from "./apiError.js";

export const createNotification = async ({
  type,
  content,
  refId,
  userId,
  transaction,
}) => {
  try {
    await db.Notification.create(
      {
        type,
        content,
        refId,
        status: "UNREAD",
        createAt: new Date(),
        userId,
      },
      { transaction }
    );
  } catch (error) {
    throw new ApiError(`Lỗi khi tạo thông báo: ${error.message}`, 500);
  }
};
