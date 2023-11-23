package com.emerchantpay.paymentsystemtask.validation.transaction;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.utils.TransactionUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import java.util.regex.Pattern;
import java.util.stream.Stream;


public interface TransactionValidator {

    Log log = LogFactory.getLog(TransactionValidator.class);
    String patternPhone =
            "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";

    String patternUuid="^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";


    default TransactionDto validate(TransactionDto transaction) throws TransactionException {

         if( transaction==null ){
             throw new TransactionException(Message.MISSING_TRANSACTION.getName());
         }

        validateUuid(transaction);
        validateSupportedStatus(transaction);
        validateStatus(transaction);
        validateEmail(transaction);
        validatePhone(transaction);
        validateAmount(transaction);
        validateReferenceId(transaction);
        return transaction;
     }

    TransactionDto validateTransaction(TransactionDto transaction) throws TransactionException;

     default void validateSupportedStatus(TransactionDto transaction) throws TransactionException {
         boolean isStatusNonMatch = Stream.of(TransactionStatus.values())
                 .noneMatch(s->s.getName().equals(transaction.getStatus()));

         if(transaction.getStatus()==null || isStatusNonMatch){
             throw new TransactionException(Message.UNSUPPORTED_TRANSACTION_STATUS.getName(),
                     transaction.getStatus(), transaction.getTransactionType());
         }
     }

    default void validateStatus(TransactionDto transaction) {

        if ( transaction.getStatus().equals(TransactionStatus.REVERSED.name())
                || transaction.getStatus().equals(TransactionStatus.REFUNDED.name()) ) {

            log.info(String.format("%s Transaction with status %s can not be imported. Check if exists possible transition!",
                    transaction.getTransactionType(),
                    transaction.getStatus()));
            transaction.setStatus(TransactionStatus.ERROR.getName());
        }
    }

    default void validateUuid(TransactionDto transaction){

         Pattern pattern = Pattern.compile(patternUuid);
         if(transaction.getUuid()==null || !pattern.matcher(transaction.getUuid()).matches()){
             transaction.setUuid(TransactionUtils.generateRandomUuid());
         }
    }

    default void validateEmail(TransactionDto transaction) throws TransactionException {

        boolean isValid = EmailValidator.getInstance().isValid(transaction.getCustomerEmail());
        if (!isValid) {
            throw new TransactionException(Message.INVALID_EMAIL.getName(), transaction.getCustomerEmail());
        }
    }

    default void validateAmount(TransactionDto transaction) throws TransactionException {

        if( transaction.getAmount()==null || transaction.getAmount()<0 ) {
            throw new TransactionException(Message.INVALID_TRANSACTION_SUM.getName(),
                    String.valueOf(transaction.getAmount()));
        }
    }

    default void validateReferenceId(TransactionDto transaction) throws TransactionException {

        if(transaction.getStatus().equals(TransactionStatus.ERROR.getName())){
            transaction.setReferenceIdentifier(null);
        }else {
            if(transaction.getReferenceIdentifier()==null){
                throw new TransactionException(Message.MISSING_TRANS_REFERENCE_IDENTIFIER.getName());
            }
        }
    }

    default void validatePhone(TransactionDto transaction) throws TransactionException {
        Pattern pattern = Pattern.compile(patternPhone);
        String phone= transaction.getCustomerPhone();

        if (phone == null || !pattern.matcher(phone).matches() ) {
            throw  new TransactionException(String.format(Message.INVALID_TELEPHONE_NUMBER.getName(),phone));
        }
    }
}
