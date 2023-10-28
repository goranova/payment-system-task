package com.emerchantpay.paymentsystemtask.validation;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;

public class ReversalValidator implements TransactionValidator {

    @Override
    public TransactionDto validate(TransactionDto transaction) {
        validateUuid(transaction);
        validateStatus(transaction);
        validateEmail(transaction);
        return transaction;
    }
}
