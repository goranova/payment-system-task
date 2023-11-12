package com.emerchantpay.paymentsystemtask.validation.transaction;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;

public class ReversalValidator implements TransactionValidator {

    @Override
    public TransactionDto validate(TransactionDto transaction) throws TransactionException {
        validateUuid(transaction);
        validateStatus(transaction);
        validateEmail(transaction);
        validateReferenceId(transaction);
        return transaction;
    }
    public TransactionDto validateTransaction(TransactionDto transaction) throws TransactionException {
        if (!transaction.getTransactionType().equals(TransactionType.REVERSAL.getName())) {
            throw new TransactionException(Message.UNSUPPORTED_TRANSACTION_TYPE.getName());
        }
        return validate(transaction);
    }
}
