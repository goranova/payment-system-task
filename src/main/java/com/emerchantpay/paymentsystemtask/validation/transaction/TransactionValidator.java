package com.emerchantpay.paymentsystemtask.validation.transaction;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.exceptions.MerchantException;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.utils.TransactionUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import java.util.UUID;
import java.util.stream.Stream;


public interface TransactionValidator {

    Log log = LogFactory.getLog(TransactionValidator.class);

     default TransactionDto validate(TransactionDto transaction) throws TransactionException, MerchantException {

         if( transaction==null ){
             throw new TransactionException(Message.MISSING_TRANSACTION.getName());
         }

        validateUuid(transaction);
        validateSupportedStatus(transaction);
        validateStatus(transaction);
        validateEmail(transaction);
        validateAmount(transaction);
        validateReferenceId(transaction);
        return transaction;
     }

    TransactionDto validateTransaction(TransactionDto transaction) throws TransactionException, MerchantException;

     default TransactionDto validateSupportedStatus(TransactionDto transaction) throws TransactionException {
         boolean isStatusNonMatch = Stream.of(TransactionStatus.values())
                 .noneMatch(s->s.getName().equals(transaction.getStatus()));

         if(transaction.getStatus()==null || isStatusNonMatch){
             throw new TransactionException(Message.UNSUPPORTED_TRANSACTION_STATUS.getName(),
                     transaction.getStatus(), transaction.getTransactionType());
         }
         return transaction;
     }

    default TransactionDto validateStatus(TransactionDto transaction) {

        if ( transaction.getStatus().equals(TransactionStatus.REVERSED.name())
                || transaction.getStatus().equals(TransactionStatus.REFUNDED.name()) ) {

            log.info(String.format("Transaction status will be set to error. " +
                    "%s status is not allowed for %s transaction",transaction.getStatus(),transaction.getTransactionType()));
            transaction.setStatus(TransactionStatus.ERROR.getName());

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

    default TransactionDto validateEmail(TransactionDto transaction) throws TransactionException {

        boolean isValid = EmailValidator.getInstance().isValid(transaction.getCustomerEmail());
        if (!isValid) {
            throw new TransactionException(Message.INVALID_EMAIL.getName(), transaction.getCustomerEmail());
        }
        return transaction;

    }

    default TransactionDto validateAmount(TransactionDto transaction) throws TransactionException {

        if( transaction.getAmount()==null || transaction.getAmount()<0 ) {
            throw new TransactionException(Message.INVALID_TRANSACTION_SUM.getName(),
                    String.valueOf(transaction.getAmount()));
        }
        return transaction;
    }

    default TransactionDto validateReferenceId(TransactionDto transaction) throws TransactionException {

        if( transaction.getReferenceIdentifier()==null
                && !transaction.getStatus().equals(TransactionStatus.ERROR.getName()) ) {
            throw new TransactionException(Message.MISSING_TRANS_REFERENCE_IDENTIFIER.getName());
        }
        if(transaction.getReferenceIdentifier()!=null
                && transaction.getStatus().equals(TransactionStatus.ERROR.getName())){
            transaction.setReferenceIdentifier(null);
        }

        return transaction;
    }
}
