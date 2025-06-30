import express from 'express';
import TransactionController from '../controllers/transaction.controller.js';
import validate from '../middlewares/validate.middleware.js';
import validators from '../validators/transaction.validation.js';
import {
  authenticate,
  authorizeRoles,
  isResourceOwner,
} from '../middlewares/auth.middleware.js';

const router = express.Router();

router.use(authenticate);

router.post(
  '/',
  validate(validators.transaction),
  TransactionController.createTransaction
);

router.get(
  '/:transactionId',
  validate(validators.transactionId, 'params'),
  TransactionController.getTransaction
);

router.get(
  '/user/:userId',
  validate(validators.getTransactions, 'params'),
  isResourceOwner,
  TransactionController.getUserTransactions
);

router.put(
  '/:transactionId',
  validate(validators.transactionId, 'params'),
  validate(validators.updateTransaction),
  authorizeRoles('admin'),
  TransactionController.updateTransaction
);

router.delete(
  '/:transactionId',
  validate(validators.transactionId, 'params'),
  authorizeRoles('admin'),
  TransactionController.deleteTransaction
);

export default router;
