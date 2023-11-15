package com.emerchantpay.paymentsystemtask.service;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.MerchantException;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.service.handler.chain.AuthorizeChain;
import com.emerchantpay.paymentsystemtask.service.handler.chain.ChainHandler;
import com.emerchantpay.paymentsystemtask.service.handler.chain.ReversalChain;
import com.emerchantpay.paymentsystemtask.validation.transaction.AuthorizeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * TransactionHandlerService set appropriate chain
 * Authorize->Charge->Refund or
 * Authorize->Reversal
 * for transaction according to its type
 * and process transaction.
 * */

@Service
public class TransactionHandlerService {
    @Autowired
    private AuthorizeChain authChain;
    @Autowired
    private ReversalChain reversalChain;
    @Autowired
    private AuthorizeValidator authorizeValidator;


    public List<TransactionDto> handleTransactions (List<TransactionDto> transactions) throws TransactionException, MerchantException {

        List<TransactionDto> handledTransaction = new ArrayList<>();
        Map<TransactionType, ChainHandler> chains = getChains();

        for (TransactionDto tr : transactions) {

            TransactionDto validatedTrans = authorizeValidator.validateTransaction(tr);
            TransactionType transactionType = TransactionType.getByValue(tr.getTransactionType()).get();
            if (chains.containsKey(transactionType)) {

                ChainHandler chain = chains.get(transactionType);
                chain.setChain();
                List<TransactionDto> handledTrans = chain.handleTransaction(validatedTrans);
                handledTransaction.addAll(handledTrans);
            }
        }
        return handledTransaction;

    }

    private Map<TransactionType, ChainHandler> getChains() {

        Map<TransactionType, ChainHandler> chains = new HashMap<>();

        chains.put(TransactionType.AUTHORIZE, authChain);
        chains.put(TransactionType.CHARGE, authChain);
        chains.put(TransactionType.REFUND, authChain);
        chains.put(TransactionType.REVERSAL, reversalChain);

        return chains;
    }

}
