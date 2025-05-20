import Joi from "joi";

const statusEnum = ["ongoing", "completed", "hiatus", "cancelled"];

export const createStorySchema = Joi.object({
  storyName: Joi.string().trim().max(255).required().messages({
    "string.empty": "Tên truyện là bắt buộc",
    "string.max": "Tên truyện không được vượt quá 255 ký tự",
  }),
  description: Joi.string().trim().max(1000).optional().messages({
    "string.max": "Mô tả không được vượt quá 1000 ký tự",
  }),
  categories: Joi.array().items(Joi.number().integer()).optional().messages({
    "array.base": "Thể loại phải là một mảng",
  }),
  coverImg: Joi.string().required().messages({
    "string.empty": "Ảnh bìa là bắt buộc",
  }),
  status: Joi.string()
    .valid(...statusEnum)
    .optional()
    .messages({ "any.only": "Trạng thái truyện không hợp lệ" }),
  price: Joi.number()
    .min(0)
    .optional()
    .messages({ "number.min": "Giá truyện phải là số không âm" }),
  pricePerChapter: Joi.number()
    .min(0)
    .optional()
    .messages({ "number.min": "Giá mỗi chương phải là số không âm" }),
});

export const updateStorySchema = createStorySchema.fork(
  ["storyName", "coverImg"],
  (field) =>
    field
      .optional()
      .messages({ "string.empty": `${field._flags.label} không được để trống` })
);

export const storyIdSchema = Joi.object({
  storyId: Joi.number().integer().required().messages({
    "number.base": "ID truyện phải là số nguyên",
  }),
});

export const getStoriesSchema = Joi.object({
  limit: Joi.number()
    .integer()
    .min(1)
    .max(100)
    .optional()
    .messages({ "number.base": "Giới hạn phải là số nguyên từ 1 đến 100" }),
  lastId: Joi.number()
    .integer()
    .optional()
    .messages({ "number.base": "lastId phải là số nguyên" }),
  orderBy: Joi.string()
    .valid("createdAt", "updatedAt", "voteNum", "viewNum")
    .optional()
    .messages({ "any.only": "Trường sắp xếp không hợp lệ" }),
  sort: Joi.string()
    .valid("ASC", "DESC")
    .optional()
    .messages({ "any.only": "Hướng sắp xếp phải là ASC hoặc DESC" }),
});

export const filterByCategorySchema = storyIdSchema.concat(getStoriesSchema);

export const filterByCategoryAndStatusSchema = Joi.object({
  categoryId: Joi.number().integer().required().messages({
    "number.base": "ID thể loại phải là số nguyên",
  }),
  status: Joi.string()
    .valid(...statusEnum)
    .required()
    .messages({ "any.only": "Trạng thái truyện không hợp lệ" }),
}).concat(getStoriesSchema);

export const searchStoriesSchema = Joi.object({
  searchTerm: Joi.string()
    .trim()
    .required()
    .messages({ "string.empty": "Từ khóa tìm kiếm là bắt buộc" }),
}).concat(getStoriesSchema);

export const filterByUserSchema = Joi.object({
  userId: Joi.number().integer().required().messages({
    "number.base": "ID người dùng phải là số nguyên",
  }),
  includeAll: Joi.boolean().optional().messages({
    "boolean.base": "includeAll phải là boolean",
  }),
}).concat(getStoriesSchema);

export const purchaseChapterSchema = Joi.object({
  storyId: Joi.number().integer().required().messages({
    "number.base": "ID truyện phải là số nguyên",
  }),
  chapterId: Joi.number().integer().required().messages({
    "number.base": "ID chương phải là số nguyên",
  }),
});

export const createChapterSchema = Joi.object({
  chapterName: Joi.string().trim().max(255).required().messages({
    "string.empty": "Tên chương là bắt buộc",
    "string.max": "Tên chương không được vượt quá 255 ký tự",
  }),
  content: Joi.string().trim().required().messages({
    "string.empty": "Nội dung chương là bắt buộc",
  }),
  ordinalNumber: Joi.number()
    .integer()
    .min(1)
    .optional()
    .messages({ "number.min": "Số thứ tự chương phải là số nguyên dương" }),
  lockedStatus: Joi.boolean()
    .optional()
    .messages({ "boolean.base": "Trạng thái khóa phải là boolean" }),
});

export const updateChapterSchema = createChapterSchema.fork(
  ["chapterName", "content"],
  (field) =>
    field
      .optional()
      .messages({ "string.empty": `${field._flags.label} không được để trống` })
);

export const chapterIdSchema = Joi.object({
  chapterId: Joi.number().integer().required().messages({
    "number.base": "ID chương phải là số nguyên",
  }),
});
