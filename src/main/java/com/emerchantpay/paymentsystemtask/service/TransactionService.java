package com.emerchantpay.paymentsystemtask.service;

import com.emerchantpay.paymentsystemtask.dao.TransactionRepository;
import com.emerchantpay.paymentsystemtask.dto.TransactionConverter;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.model.AuthorizeTransaction;
import com.emerchantpay.paymentsystemtask.model.ChargeTransaction;
import com.emerchantpay.paymentsystemtask.model.RefundTransaction;
import com.emerchantpay.paymentsystemtask.model.ReversalTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository trans;


    public TransactionDto saveTransaction(AuthorizeTransaction auth) {
        trans.save(auth);
        return TransactionConverter.convertToTransactionDto(auth);
    }

    public TransactionDto saveTransaction(ChargeTransaction charge) {
        trans.save(charge);
        return TransactionConverter.convertToTransactionDto(charge);
    }

    public TransactionDto saveTransaction(RefundTransaction refund) {
        trans.save(refund);
        return TransactionConverter.convertToTransactionDto(refund);
    }

    public TransactionDto saveTransaction(ReversalTransaction reversal) {
        trans.save(reversal);
        return TransactionConverter.convertToTransactionDto(reversal);
    }


    public List<TransactionDto> findAuthorizeTransactions() {
        List<AuthorizeTransaction> auth = trans.findAuthorizeTransactions();
        return auth.stream()
                .map(TransactionConverter::convertToTransactionDto)
                .collect(Collectors.toList());

    }

    public List<TransactionDto> findChargeTransactions() {
        List<ChargeTransaction> charge = trans.findChargeTransactions();
        return charge.stream()
                .map(TransactionConverter::convertToTransactionDto)
                .collect(Collectors.toList());

    }

    public TransactionDto findChargeTransactionByRefId(String referenceId, TransactionStatus status) {
        ChargeTransaction charge = trans.findChargeTransactionByRefId(referenceId, status);
        TransactionDto transaction = TransactionConverter.convertToTransactionDto(charge);
        return transaction;
    }

    public TransactionDto findAuthorizeTransactionByRefId(String referenceId, TransactionStatus status) {
        AuthorizeTransaction auth = trans.findAuthorizeTransactionByRefId(referenceId, status);
        TransactionDto transaction = TransactionConverter.convertToTransactionDto(auth);
        return transaction;
    }


    public List<TransactionDto> findRefundTransactions() {
        List<RefundTransaction> refund = trans.findRefundTransactions();
        return refund.stream()
                .map(TransactionConverter::convertToTransactionDto)
                .collect(Collectors.toList());

    }


    public List<TransactionDto> findReversalTransactions() {
        List<RefundTransaction> reversal = trans.findReversalTransactions();
        return reversal.stream()
                .map(TransactionConverter::convertToTransactionDto)
                .collect(Collectors.toList());

    }


}
