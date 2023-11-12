package com.emerchantpay.paymentsystemtask.validation.transaction;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;

public class AuthorizeValidator implements TransactionValidator {

    @Override
    public TransactionDto validateStatus(TransactionDto transaction) throws TransactionException {
        if (!transaction.getStatus().equals(TransactionStatus.APPROVED.name())
                && !transaction.getStatus().equals(TransactionStatus.REVERSED.name())
                && !transaction.getStatus().equals(TransactionStatus.ERROR.name())) {

            throw new TransactionException(Message.INVALID_TRANSACTION_STATUS.getName(),
                    transaction.getStatus(), transaction.getTransactionType());
        }
        return transaction;
    }

    public TransactionDto validateTransaction(TransactionDto transaction) throws TransactionException {
        if(transaction.getTransactionType().equals(TransactionType.AUTHORIZE.getName())){
           return validate(transaction);
        }else return new ChargeValidator().validateTransaction(transaction);
    }

}
