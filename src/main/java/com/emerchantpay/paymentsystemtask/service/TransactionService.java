package com.emerchantpay.paymentsystemtask.service;

import com.emerchantpay.paymentsystemtask.dao.TransactionRepository;
import com.emerchantpay.paymentsystemtask.dto.TransactionConverter;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
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


    public TransactionDto findChargeTransactionByRefId(String referenceId, TransactionStatus status) throws TransactionException {
       List<ChargeTransaction> chargeTransactions = trans.findChargeTransactionByRefId(referenceId, status);
       if(chargeTransactions.isEmpty()){
           return null;
       }
       if(chargeTransactions.size()==1){
           ChargeTransaction charge = chargeTransactions.stream().findFirst().get();
           return TransactionConverter.convertToTransactionDto(charge);
        }else{
           throw new TransactionException(Message.MULTIPLE_CHARGE_TRANSACTION.getName(),referenceId);
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
