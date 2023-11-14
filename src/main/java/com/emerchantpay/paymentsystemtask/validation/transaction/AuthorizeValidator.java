package com.emerchantpay.paymentsystemtask.validation.transaction;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;

public class AuthorizeValidator implements TransactionValidator {

    @Override
    public TransactionDto validateStatus(TransactionDto transaction) {

        if (transaction.getStatus().equals(TransactionStatus.REFUNDED.getName())) {

            log.info(String.format("Transaction status will be set to error. " +
                    "%s status is not allowed for %s transaction",transaction.getStatus(),transaction.getTransactionType()));
            transaction.setStatus(TransactionStatus.ERROR.getName());
        }
        return transaction;
    }

    public TransactionDto validateTransaction(TransactionDto transaction) throws TransactionException {
        if(transaction.getTransactionType().equals(TransactionType.AUTHORIZE.getName())){
           return validate(transaction);
        }else return new ChargeValidator().validateTransaction(transaction);
    }

    public TransactionDto validateReferenceId(TransactionDto transaction){
        if(transaction.getReferenceIdentifier()!=null){
            transaction.setReferenceIdentifier(null);
        }
        return transaction;
    }
}
