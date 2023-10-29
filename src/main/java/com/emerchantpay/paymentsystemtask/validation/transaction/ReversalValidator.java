package com.emerchantpay.paymentsystemtask.validation.transaction;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;

public class ReversalValidator implements TransactionValidator {

    @Override
    public TransactionDto validate(TransactionDto transaction) {
        validateUuid(transaction);
        validateStatus(transaction);
        validateEmail(transaction);
        return transaction;
    }
    public TransactionDto validateTransaction(TransactionDto transactionDto){
        if (!transactionDto.getTransactionType().equals(TransactionType.REVERSAL.getName())) {
            transactionDto.setStatus(TransactionStatus.ERROR.name());
        }
        return validate(transactionDto);
    }
}
