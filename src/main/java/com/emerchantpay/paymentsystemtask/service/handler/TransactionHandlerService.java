package com.emerchantpay.paymentsystemtask.service.handler;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.service.TransactionService;
import com.emerchantpay.paymentsystemtask.service.handler.chain.AuthorizeChain;
import com.emerchantpay.paymentsystemtask.service.handler.chain.ChainHandler;
import com.emerchantpay.paymentsystemtask.service.handler.chain.ReversalChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionHandlerService {
    @Autowired
    AuthorizeChain authChain;
    @Autowired
    ReversalChain reversalChain;
    @Autowired
    TransactionService service;


    public  List<TransactionDto> handleTransactionChain(List<TransactionDto> transactions){
        Map<TransactionType, ChainHandler> chains = new HashMap<>();
        List<TransactionDto> handledTransaction = new ArrayList<>();

        chains.put(TransactionType.AUTHORIZE,authChain);
        chains.put(TransactionType.CHARGE,authChain);
        chains.put(TransactionType.REFUND, authChain);
        chains.put(TransactionType.REVERSAL, reversalChain);
        transactions.forEach(tr->{
            TransactionType transactionType = TransactionType.getByValue(tr.getTransactionType()).get();
            if(chains.containsKey(transactionType )){
                ChainHandler chain = chains.get(transactionType);
                chain.setChain();
                handledTransaction.addAll(chain.handleTransaction(tr));
            }

        });

        return handledTransaction;

    }

    public List<TransactionDto> getAllTransactions() {
        List<TransactionDto> transactionDto = new ArrayList<>();
        transactionDto.addAll(service.findAuthorizeTransactions());
        transactionDto.addAll(service.findChargeTransactions());
        transactionDto.addAll(service.findRefundTransactions());
        transactionDto.addAll(service.findReversalTransactions());
        return transactionDto;
    }

}
