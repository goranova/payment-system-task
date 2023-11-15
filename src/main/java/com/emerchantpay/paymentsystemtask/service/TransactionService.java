package com.emerchantpay.paymentsystemtask.service;

import com.emerchantpay.paymentsystemtask.dao.TransactionRepository;
import com.emerchantpay.paymentsystemtask.dto.TransactionConverter;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.model.transaction.AuthorizeTransaction;
import com.emerchantpay.paymentsystemtask.model.transaction.ChargeTransaction;
import com.emerchantpay.paymentsystemtask.model.transaction.Transaction;
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
        List<Transaction> transactions = trans.findTransactions();
        return transactions.stream()
                .map(TransactionConverter::convertToTransactionDto)
                .collect(Collectors.toList());
    }

    public TransactionDto findNonRefundedChargeTransByRefId(String referenceId, TransactionStatus status) throws TransactionException {

        boolean isRefundExists =
                trans.existsByTransactionTypeAndReferenceIdentifier(TransactionType.REFUND.getName(),referenceId);
        if(isRefundExists){
            throw new TransactionException(Message.MULTIPLE_TRANSACTIONS.getName(), TransactionType.REFUND.getName(), referenceId);
        }

       ChargeTransaction chargeTransaction = trans.findChargeTransactionByRefId(referenceId,status);
       if(chargeTransaction!=null){
           return TransactionConverter.convertToTransactionDto(chargeTransaction);
       }
       return null;
    }

    public TransactionDto findNonReferencedAuthTransByRefId(String referenceId, TransactionStatus status) throws TransactionException {

        boolean isChargeExists =
                trans.existsByTransactionTypeAndReferenceIdentifier(TransactionType.CHARGE.getName(),referenceId);
        if(isChargeExists){
            throw new TransactionException(Message.MULTIPLE_TRANSACTIONS.getName(), TransactionType.CHARGE.getName(), referenceId);
        }

        AuthorizeTransaction auth = trans.findAuthorizeTransactionByRefId(referenceId,status);
        if(auth!=null){
            return TransactionConverter.convertToTransactionDto(auth);
        }
        return  null;
    }

    public void deleteTransOlderThanHour(Timestamp timestamp){
        trans.deleteTranOlderThanHour(timestamp);
    }


}
