package com.emerchantpay.paymentsystemtask.validation.transaction;

import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.MerchantException;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorizeValidator implements TransactionValidator {
    @Autowired
    MerchantService merchantService;

    @Override
    public TransactionDto validate(TransactionDto transaction) throws TransactionException, MerchantException {
        TransactionValidator.super.validate(transaction);
        validateMerchant(transaction);
        return transaction;
    }

    @Override
    public TransactionDto validateStatus(TransactionDto transaction) {

        if (transaction.getStatus().equals(TransactionStatus.REFUNDED.getName())) {

            log.info(String.format("Transaction status will be set to error. " +
                    "%s status is not allowed for %s transaction",transaction.getStatus(),transaction.getTransactionType()));
            transaction.setStatus(TransactionStatus.ERROR.getName());
        }
        return transaction;
    }

    public TransactionDto validateTransaction(TransactionDto transaction) throws TransactionException, MerchantException {
        if(transaction.getTransactionType().equals(TransactionType.AUTHORIZE.getName())){
           return validate(transaction);
        }else return new ChargeValidator().validateTransaction(transaction);
    }

    public TransactionDto validateReferenceId(TransactionDto transaction){
        if(transaction.getReferenceIdentifier()!=null){
            transaction.setReferenceIdentifier(null);
        }
        return transaction;
    }

    public TransactionDto validateMerchant(TransactionDto transaction) throws MerchantException {

        MerchantDto merchant = transaction.getMerchant();
        if(merchant!=null){

            merchant.setTransactions(List.of(transaction));
            MerchantDto validMer = merchantService.processMerchant(transaction.getMerchant());
            transaction.setMerchant(validMer);
            return transaction;

        }else {
            throw new MerchantException(Message.MISSING_MERCHANT.getName());
        }
    }
}
