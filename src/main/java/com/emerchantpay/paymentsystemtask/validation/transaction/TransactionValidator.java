package com.emerchantpay.paymentsystemtask.validation.transaction;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.utils.TransactionUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.UUID;


public interface TransactionValidator {

     default TransactionDto validate(TransactionDto transaction) throws TransactionException {
        validateUuid(transaction);
        validateStatus(transaction);
        validateEmail(transaction);
        validateAmount(transaction);
        return transaction;
     }

    TransactionDto validateTransaction(TransactionDto transaction) throws TransactionException;

    default TransactionDto validateStatus(TransactionDto transaction) throws TransactionException {
        if (!transaction.getStatus().equals(TransactionStatus.APPROVED.name())
                && !transaction.getStatus().equals(TransactionStatus.ERROR.name())) {

            throw new TransactionException(Message.INVALID_TRANSACTION_STATUS.getName(),
                    transaction.getStatus(), transaction.getTransactionType());
        }
        return transaction;
    }

    default TransactionDto validateUuid(TransactionDto transaction){
        if(transaction.getUuid()!=null){
            String validUuid = UUID.fromString(transaction.getUuid()).toString();
            if (validUuid!=null && validUuid.equals(transaction.getUuid()) ){
                return transaction;
            }
        }

        transaction.setUuid(TransactionUtils.generateRandomUuid());
        return transaction;
    }

    default TransactionDto validateEmail(TransactionDto transaction){
        boolean isValid = EmailValidator.getInstance().isValid(transaction.getCustomerEmail());
        if (isValid) {
            return transaction;
        }
        transaction.setCustomerEmail(null);
        return transaction;

    }

    default TransactionDto validateAmount(TransactionDto transaction) throws TransactionException {

        if( transaction.getAmount()==null || transaction.getAmount()<0 ) {
            throw new TransactionException(Message.INVALID_TRANSACTION_SUM.getName(),
                    String.valueOf(transaction.getAmount()));
        }
        return transaction;
    }

    default TransactionDto validateReferenceId(TransactionDto transaction){
        if(transaction.getReferenceIdentifier()!=null) {
            return transaction;
        }
        transaction.setStatus(TransactionStatus.ERROR.name());
        return transaction;
    }
}
