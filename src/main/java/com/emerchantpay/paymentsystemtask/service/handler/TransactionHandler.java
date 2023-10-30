package com.emerchantpay.paymentsystemtask.service.handler;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;

import java.util.List;

/* TransactionHandler processes Transaction according to its type
   when the chain is set :
   Authorize->Charge->Refund
   Authorize->Reversal
   The transaction is processed

* */
public abstract class TransactionHandler {
    protected TransactionHandler nextTransition;

    protected   abstract List<TransactionDto> handleTransaction(TransactionDto dto) ;

    public void setNextTransition(TransactionHandler nextTransition) {
        this.nextTransition = nextTransition;
    }


}
