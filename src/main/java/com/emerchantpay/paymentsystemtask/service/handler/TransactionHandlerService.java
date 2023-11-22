package com.emerchantpay.paymentsystemtask.service.handler;

import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.MerchantException;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.response.ResponseTransactionMessage;
import com.emerchantpay.paymentsystemtask.service.AuthenticationService;
import com.emerchantpay.paymentsystemtask.service.handler.chain.AuthorizeChain;
import com.emerchantpay.paymentsystemtask.service.handler.chain.ChainHandler;
import com.emerchantpay.paymentsystemtask.service.handler.chain.ReversalChain;
import com.emerchantpay.paymentsystemtask.validation.transaction.AuthorizeValidator;
import com.emerchantpay.paymentsystemtask.validation.transaction.TransactionValidator;
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
    private AuthenticationService authenticationService;

    public ResponseTransactionMessage handleTransactions (List<TransactionDto> transactions) throws TransactionException, MerchantException {

        List<TransactionDto> handledTransaction = new ArrayList<>();
        ResponseTransactionMessage response= new ResponseTransactionMessage("", handledTransaction);
        Map<TransactionType, ChainHandler> chains = getChains();

        for (TransactionDto tr : transactions) {

            TransactionValidator validator = new AuthorizeValidator();
            TransactionDto validatedTrans = validator.validateTransaction(tr);
            setAuthenticatedMerchant(validatedTrans);

            TransactionType transactionType = TransactionType.getByValue(tr.getTransactionType()).get();
            if (chains.containsKey(transactionType)) {

                ChainHandler chain = chains.get(transactionType);
                chain.setChain();
                return chain.handleTransaction(validatedTrans);
            }
        }
        return response;

    }

    private Map<TransactionType, ChainHandler> getChains() {

        Map<TransactionType, ChainHandler> chains = new HashMap<>();

        chains.put(TransactionType.AUTHORIZE, authChain);
        chains.put(TransactionType.CHARGE, authChain);
        chains.put(TransactionType.REFUND, authChain);
        chains.put(TransactionType.REVERSAL, reversalChain);

        return chains;
    }

    public void setAuthenticatedMerchant(TransactionDto transaction) throws MerchantException {

        MerchantDto merchant = authenticationService.getAuthenticatedMerchant();
        if ( merchant!=null ){
            transaction.setMerchant(merchant);
        } else {
            throw  new MerchantException(Message.MISSING_MERCHANT.getName());
        }
    }
}
