package com.emerchantpay.paymentsystemtask.validation.transaction;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.MerchantException;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;

public class RefundValidator implements TransactionValidator {

    public TransactionDto validateTransaction(TransactionDto transaction) throws TransactionException, MerchantException {
        if(transaction.getTransactionType().equals(TransactionType.REFUND.getName())){
            return validate(transaction);
        }else return new ReversalValidator().validateTransaction(transaction);
    }
}
