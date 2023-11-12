package com.emerchantpay.paymentsystemtask.service.handler.chain;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.service.handler.AuthorizeHandler;
import com.emerchantpay.paymentsystemtask.validation.transaction.AuthorizeValidator;
import com.emerchantpay.paymentsystemtask.validation.transaction.TransactionValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
/*ChainHandler is usded to define chains for Transaction Transitions
* Authorize->Charge->Refund
* Authorize->Reversal
* */
public abstract class  ChainHandler {
    @Autowired
    AuthorizeHandler authorize;

   public abstract void  setChain();

    public List<TransactionDto> handleTransaction(TransactionDto transaction) {
        return authorize.handleTransaction(transaction);
    }

}
