package com.emerchantpay.paymentsystemtask.service.handler;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.response.ResponseTransactionMessage;

import java.util.List;

/* TransactionHandler processes Transaction according to its type
   when the chain is set :
   Authorize->Charge->Refund
   Authorize->Reversal
   The transaction is processed

* */
public abstract class TransactionHandler {
    protected TransactionHandler nextTransition;

    protected String message;

    protected   abstract ResponseTransactionMessage handleTransaction(TransactionDto dto) throws TransactionException;

    public void setNextTransition(TransactionHandler nextTransition) {
        this.nextTransition = nextTransition;
    }
}
