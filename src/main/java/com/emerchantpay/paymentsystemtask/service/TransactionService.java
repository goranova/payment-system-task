package com.emerchantpay.paymentsystemtask.service;

import com.emerchantpay.paymentsystemtask.dao.TransactionRepository;
import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
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
import java.util.HashSet;
import java.util.Set;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository trans;

    @Autowired
    private AuthenticationService authenticationService;

    public TransactionDto saveTransaction(Transaction transaction) {
        Transaction savedTrans = trans.save(transaction);
        return TransactionConverter.convertToTransactionDto(savedTrans);
    }


    public Set<TransactionDto> findAuthenticatedUserTransactions(){
       MerchantDto merchant = authenticationService.getAuthenticatedMerchant();
       if(merchant!=null){
           return merchant.getTransactions();
       }else return new HashSet<>();
    }

    public TransactionDto findNonRefundedChargeTransByRefIdMerId(String referenceId,  TransactionStatus status, String merchantId) throws TransactionException {

        boolean isRefundExists =
                trans.existsByTransactionTypeAndReferenceIdentifier(TransactionType.REFUND.getName(),referenceId);
        if(isRefundExists){
            throw new TransactionException(Message.MULTIPLE_TRANSACTIONS.getName(), TransactionType.REFUND.getName(), referenceId);
        }

       ChargeTransaction chargeTransaction = trans.findChargeTransactionByRefIdMerId(referenceId, status,merchantId);
       if(chargeTransaction!=null){
           return TransactionConverter.convertToTransactionDto(chargeTransaction);
       }
       return null;
    }

    public TransactionDto findNonReferencedAuthTransByRefIdMerId(String referenceId, TransactionStatus status, String merchantId) throws TransactionException {

        boolean isChargeExists =
                trans.existsByTransactionTypeAndReferenceIdentifier(TransactionType.CHARGE.getName(),referenceId);
        if(isChargeExists){
            throw new TransactionException(Message.MULTIPLE_TRANSACTIONS.getName(), TransactionType.CHARGE.getName(), referenceId);
        }

        AuthorizeTransaction auth = trans.findAuthorizeTransactionByRefIdMerId(referenceId,status, merchantId);
        if(auth!=null){
            return TransactionConverter.convertToTransactionDto(auth);
        }
        return  null;
    }

    public void deleteTransOlderThanHour(Timestamp timestamp){
        trans.deleteTranOlderThanHour(timestamp);
    }
}
