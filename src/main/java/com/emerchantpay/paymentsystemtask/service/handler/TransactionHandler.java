package com.emerchantpay.paymentsystemtask.service.handler;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;

import java.util.List;


public abstract class TransactionHandler {
    protected TransactionHandler nextTransition;

    protected   abstract List<TransactionDto> handleTransaction(TransactionDto dto) ;

    public void setNextTransition(TransactionHandler nextTransition) {
        this.nextTransition = nextTransition;
    }


}
