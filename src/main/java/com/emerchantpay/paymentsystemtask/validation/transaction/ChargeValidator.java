package com.emerchantpay.paymentsystemtask.validation.transaction;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.MerchantException;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;

public class ChargeValidator implements TransactionValidator {

    @Override
    public TransactionDto validateStatus(TransactionDto transaction)  {
        if (transaction.getStatus().equals(TransactionStatus.REVERSED.getName())) {

            log.info(String.format("Transaction status will be set to error. " +
                    "%s status is not allowed for %s transaction",transaction.getStatus(),transaction.getTransactionType()));
            transaction.setStatus(TransactionStatus.ERROR.getName());
        }
        return transaction;
    }

    @Override
    public TransactionDto validateTransaction(TransactionDto transaction) throws TransactionException, MerchantException {
        if(transaction.getTransactionType().equals(TransactionType.CHARGE.getName())){
            return validate(transaction);
        }else return new RefundValidator().validateTransaction(transaction);
    }
}
