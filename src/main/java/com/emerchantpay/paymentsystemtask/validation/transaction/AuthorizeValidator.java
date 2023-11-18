package com.emerchantpay.paymentsystemtask.validation.transaction;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.MerchantException;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;


public class AuthorizeValidator implements TransactionValidator {

    public TransactionDto validateTransaction(TransactionDto transaction) throws TransactionException, MerchantException {
       if(transaction.getTransactionType()==null){
           throw new TransactionException(Message.UNSUPPORTED_TRANSACTION_TYPE.getName(),
                   transaction.getTransactionType());
       }
        if(transaction.getTransactionType().equals(TransactionType.AUTHORIZE.getName())){
           return validate(transaction);
        } else return new ChargeValidator().validateTransaction(transaction);
    }

    public TransactionDto validateReferenceId(TransactionDto transaction){
        if(transaction.getReferenceIdentifier()!=null){
            transaction.setReferenceIdentifier(null);
        }
        return transaction;
    }
}
