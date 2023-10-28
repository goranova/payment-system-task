package com.emerchantpay.paymentsystemtask.validation;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;

public class RefundValidator implements TransactionValidator {

    @Override
    public TransactionDto validate(TransactionDto transaction) {
        validateUuid(transaction);
        validateStatus(transaction);
        validateEmail(transaction);
        validateAmount(transaction);
        validateReferenceId(transaction);
        return transaction;
    }
}
