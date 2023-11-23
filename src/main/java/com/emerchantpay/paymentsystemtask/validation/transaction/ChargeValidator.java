package com.emerchantpay.paymentsystemtask.validation.transaction;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;

public class ChargeValidator implements TransactionValidator {

    @Override
    public TransactionDto validateTransaction(TransactionDto transaction) throws TransactionException {
        if(transaction.getTransactionType().equals(TransactionType.CHARGE.getName())){
            return validate(transaction);
        }else return new RefundValidator().validateTransaction(transaction);
    }
}
