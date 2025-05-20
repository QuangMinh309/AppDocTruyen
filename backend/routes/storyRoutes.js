// routes/storyRoutes.js
import express from "express";
import validate from "../middlewares/validate.js";
import StoryValidation from "../validators/storyValidation.js";
import { StoryController } from "../controllers/storyController.js";

const router = express.Router();

router.post(
  "/",
  validate(StoryValidation.storyCreateSchema),
  StoryController.create
);

router.get(
  "/:storyId",
  validate(StoryValidation.idSchema),
  StoryController.get
);

router.put(
  "/",
  validate(StoryValidation.storyUpdateSchema),
  StoryController.update
);

router.delete(
  "/:storyId",
  validate(StoryValidation.idSchema),
  StoryController.delete
);

router.patch(
  "/:storyId/view",
  validate(StoryValidation.idSchema),
  StoryController.updateViewNum
);

export default router;
