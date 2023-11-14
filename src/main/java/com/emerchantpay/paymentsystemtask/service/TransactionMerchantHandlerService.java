package com.emerchantpay.paymentsystemtask.service;

import com.emerchantpay.paymentsystemtask.dto.MerchantConverter;
import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.MerchantException;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.service.handler.chain.AuthorizeChain;
import com.emerchantpay.paymentsystemtask.service.handler.chain.ChainHandler;
import com.emerchantpay.paymentsystemtask.service.handler.chain.ReversalChain;
import com.emerchantpay.paymentsystemtask.validation.MerchantValidator;
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
public class TransactionMerchantHandlerService {
    @Autowired
    private AuthorizeChain authChain;
    @Autowired
    private ReversalChain reversalChain;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private MerchantService merchantService;


    public List<TransactionDto> handleTransactions (List<TransactionDto> transactions) throws TransactionException, MerchantException {

        List<TransactionDto> handledTransaction = new ArrayList<>();
        Map<TransactionType, ChainHandler> chains = getChains();

        for (TransactionDto tr : transactions) {
            TransactionDto validatedTrans = validateTransaction(tr);

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

    public List<MerchantDto> handleMerchants(List<MerchantDto> merchants) throws MerchantException, TransactionException {
        List<MerchantDto> handledMerchants = new ArrayList<>();
        for (MerchantDto mr : merchants) {
            if (mr.getTransactions().isEmpty()) {

                MerchantValidator validator = new MerchantValidator();
                MerchantDto validMerchant = validator.validate(mr);
                MerchantDto merchant = merchantService.findMerchantByDescrStatus(validMerchant);

                MerchantDto savedMerchant = merchantService.save(MerchantConverter.convertToMerchant(merchant));
                handledMerchants.add(savedMerchant);
            }else {

                mr.getTransactions().stream()
                        .forEach(tr -> tr.setMerchant(mr));
                handleTransactions(mr.getTransactions());
            }
        }

        return handledMerchants;
    }

    public List<TransactionDto> findTransactions() {
        return transactionService.findTransactions();
    }

    private Map<TransactionType, ChainHandler> getChains() {

        Map<TransactionType, ChainHandler> chains = new HashMap<>();

        chains.put(TransactionType.AUTHORIZE, authChain);
        chains.put(TransactionType.CHARGE, authChain);
        chains.put(TransactionType.REFUND, authChain);
        chains.put(TransactionType.REVERSAL, reversalChain);

        return chains;
    }

    private TransactionDto validateTransaction(TransactionDto trans) throws TransactionException, MerchantException {

        TransactionValidator validator = new AuthorizeValidator();
        TransactionDto validatedTrans = validator.validateTransaction(trans);

        MerchantDto merchant = validatedTrans.getMerchant();
        merchant.setTransactions(List.of(validatedTrans));

        MerchantValidator merchantValidator = new MerchantValidator();
        MerchantDto validatedMerchant = merchantValidator.validate(merchant);
        MerchantDto existingMerchant = merchantService.findMerchantByDescrStatus(validatedMerchant);

        validatedTrans.setMerchant(existingMerchant);

        return validatedTrans;
    }


}
