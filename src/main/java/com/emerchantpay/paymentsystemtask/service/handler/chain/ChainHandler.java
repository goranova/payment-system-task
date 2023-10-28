package com.emerchantpay.paymentsystemtask.service.handler.chain;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.service.handler.AuthorizeHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class  ChainHandler {
    @Autowired
    AuthorizeHandler authorize;

   public abstract void  setChain();

    public List<TransactionDto> handleTransaction(TransactionDto dto) {
        return authorize.handleTransaction(dto);
    }

}
