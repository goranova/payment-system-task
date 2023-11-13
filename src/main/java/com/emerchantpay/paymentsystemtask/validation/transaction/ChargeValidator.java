package com.emerchantpay.paymentsystemtask.validation.transaction;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

public class ChargeValidator implements TransactionValidator {

    @Override
    public TransactionDto validateStatus(TransactionDto transaction)  {
        if (transaction.getStatus().equals(TransactionStatus.REVERSED.getName())) {

            transaction.setStatus(TransactionStatus.ERROR.getName());
            transaction.setReferenceIdentifier(null);
            log.info(String.format("Transaction status is set to error. " +
                    "%s status is not allowed for %s transaction",transaction.getStatus(),transaction.getTransactionType()));
        }
        return transaction;
    }

    @Override
    public TransactionDto validateTransaction(TransactionDto transaction) throws TransactionException {
        if(transaction.getTransactionType().equals(TransactionType.CHARGE.getName())){
            return validate(transaction);
        }else return new RefundValidator().validateTransaction(transaction);
    }
}
