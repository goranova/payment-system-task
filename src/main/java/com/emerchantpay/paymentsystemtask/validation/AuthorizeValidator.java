package com.emerchantpay.paymentsystemtask.validation;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;

public class AuthorizeValidator implements TransactionValidator {

    @Override
    public TransactionDto validateStatus(TransactionDto transactionDto) {
        if (!transactionDto.getStatus().equals(TransactionStatus.APPROVED.name())
                && !transactionDto.getStatus().equals(TransactionStatus.REVERSED.name())
                && !transactionDto.getStatus().equals(TransactionStatus.ERROR.name())) {
            transactionDto.setStatus(TransactionStatus.ERROR.name());
        }
        return transactionDto;
    }

}
