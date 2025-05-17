import ApiError from "../utils/apiError";
import { Op } from "sequelize";
import { Chapter } from "../models/entities/chapter";

class ChapterService {
  // Tạo chapter mới
  async createChapter(data) {
    const data = {
      ...data,
      viewNum: 0,
    };
    const chapter = await Chapter.create(data);
    return chapter;
  }

  // Lấy chapter theo ID
}

export default ChapterService();
