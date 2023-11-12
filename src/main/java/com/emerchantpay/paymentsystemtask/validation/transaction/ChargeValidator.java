package com.emerchantpay.paymentsystemtask.validation.transaction;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;

public class ChargeValidator implements TransactionValidator {

    @Override
    public TransactionDto validate(TransactionDto transaction) throws TransactionException {
        validateUuid(transaction);
        validateStatus(transaction);
        validateEmail(transaction);
        validateAmount(transaction);
        validateReferenceId(transaction);
        return transaction;
    }

    @Override
    public TransactionDto validateStatus(TransactionDto transaction) throws TransactionException {
        if (!transaction.getStatus().equals(TransactionStatus.APPROVED.name())
                && !transaction.getStatus().equals(TransactionStatus.REFUNDED.name())
                && !transaction.getStatus().equals(TransactionStatus.ERROR.name())) {

            throw new TransactionException(Message.INVALID_TRANSACTION_STATUS.getName(),
                    transaction.getStatus(), transaction.getTransactionType());
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
