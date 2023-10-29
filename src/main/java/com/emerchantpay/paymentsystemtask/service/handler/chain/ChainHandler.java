package com.emerchantpay.paymentsystemtask.service.handler.chain;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.service.handler.AuthorizeHandler;
import com.emerchantpay.paymentsystemtask.validation.transaction.AuthorizeValidator;
import com.emerchantpay.paymentsystemtask.validation.transaction.TransactionValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class  ChainHandler {
    @Autowired
    AuthorizeHandler authorize;

   public abstract void  setChain();

    public List<TransactionDto> handleTransaction(TransactionDto dto) {
        TransactionValidator validator = new AuthorizeValidator();
        TransactionDto validatedTransaction = validator.validateTransaction(dto);
        return authorize.handleTransaction(validatedTransaction);
    }

}
