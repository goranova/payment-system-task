package com.emerchantpay.paymentsystemtask.validation.transaction;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;

public class RefundValidator implements TransactionValidator {

    @Override
    public TransactionDto validate(TransactionDto transaction) throws TransactionException {
        validateUuid(transaction);
        validateStatus(transaction);
        validateEmail(transaction);
        validateAmount(transaction);
        validateReferenceId(transaction);
        return transaction;
    }

    public TransactionDto validateTransaction(TransactionDto transaction) throws TransactionException {
        if(transaction.getTransactionType().equals(TransactionType.REFUND.getName())){
            return validate(transaction);
        }else return new ReversalValidator().validateTransaction(transaction);
    }
}
