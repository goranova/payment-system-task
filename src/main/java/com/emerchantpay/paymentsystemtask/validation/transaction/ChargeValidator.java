package com.emerchantpay.paymentsystemtask.validation.transaction;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;

public class ChargeValidator implements TransactionValidator {

    @Override
    public TransactionDto validateStatus(TransactionDto transactionDto) {
        if (!transactionDto.getStatus().equals(TransactionStatus.APPROVED.name())
                && !transactionDto.getStatus().equals(TransactionStatus.REFUNDED.name())
                && !transactionDto.getStatus().equals(TransactionStatus.ERROR.name())) {

            transactionDto.setStatus(TransactionStatus.ERROR.name());
        }
        return transactionDto;
    }

    @Override
    public TransactionDto validateTransaction(TransactionDto transactionDto){
        if(transactionDto.getTransactionType().equals(TransactionType.CHARGE.getName())){
            return validate(transactionDto);
        }else return new RefundValidator().validateTransaction(transactionDto);
    }
}
