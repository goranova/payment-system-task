package com.emerchantpay.paymentsystemtask.service;

import com.emerchantpay.paymentsystemtask.dao.TransactionRepository;
import com.emerchantpay.paymentsystemtask.dto.TransactionConverter;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.model.transaction.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository trans;


    public TransactionDto saveTransaction(Transaction transaction) {
        Transaction savedTrans = trans.save(transaction);
        return TransactionConverter.convertToTransactionDto(savedTrans);
    }



    public List<TransactionDto> findTransactions() {
        List<Transaction> auth = trans.findTransactions();
        return auth.stream()
                .map(TransactionConverter::convertToTransactionDto)
                .collect(Collectors.toList());

    }


    public TransactionDto findChargeTransactionByRefId(String referenceId, TransactionStatus status) {
        ChargeTransaction charge = trans.findChargeTransactionByRefId(referenceId, status);
        if(charge!=null){
            TransactionDto transaction = TransactionConverter.convertToTransactionDto(charge);
            return transaction;
        }else{
            return  null;
        }
    }

    public TransactionDto findAuthorizeTransactionByRefId(String referenceId, TransactionStatus status) {
        AuthorizeTransaction auth = trans.findAuthorizeTransactionByRefId(referenceId, status);
        if(auth!=null){
            TransactionDto transaction = TransactionConverter.convertToTransactionDto(auth);
            return transaction;
        }
        return  null;
    }

    public void deleteTransOlderThanHour(Timestamp timestamp){
        trans.deleteTranOlderThanHour(timestamp);
    }


}
