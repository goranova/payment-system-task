package com.emerchantpay.paymentsystemtask.validation.transaction;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;

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

    public TransactionDto validateTransaction(TransactionDto transactionDto){
        if(transactionDto.getTransactionType().equals(TransactionType.REFUND.getName())){
            return validate(transactionDto);
        }else return new ReversalValidator().validateTransaction(transactionDto);
    }
}
