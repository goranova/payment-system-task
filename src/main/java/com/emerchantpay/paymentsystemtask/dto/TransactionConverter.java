package com.emerchantpay.paymentsystemtask.dto;

import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.model.transaction.*;

import java.util.HashMap;
import java.util.Map;

public class TransactionConverter {
    private static final Map<String, Transaction> map = new HashMap<>();

    static {

        map.put(TransactionType.AUTHORIZE.getName(), new AuthorizeTransaction());
        map.put(TransactionType.CHARGE.getName(), new ChargeTransaction());
        map.put(TransactionType.REFUND.getName(), new RefundTransaction());
        map.put(TransactionType.REVERSAL.getName(), new ReversalTransaction());
    }

    public static TransactionDto convertToTransactionDto(Transaction transaction) {

        TransactionDto dto = new TransactionDto();
        dto.setUuid(transaction.getUuid());
        dto.setStatus(transaction.getStatus().name());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setCustomerEmail(transaction.getCustomerEmail());
        dto.setCustomerPhone(transaction.getCustomerPhone());
        dto.setReferenceIdentifier(transaction.getReferenceIdentifier());
        dto.setAmount(transaction.getAmount());
        dto.setMerchant(MerchantConverter.convertToMerchantDto(transaction.getMerchant()));
        dto.setTimestamp(transaction.getTimestamp().toString());
        return dto;
    }


    public static Transaction convertToTransaction(TransactionDto dto){

        Transaction transaction = map.get(dto.getTransactionType());
        transaction.setUuid(dto.getUuid());
        transaction.setAmount(dto.getAmount());
        transaction.setCustomerEmail(dto.getCustomerEmail());
        transaction.setCustomerPhone(dto.getCustomerPhone());
        transaction.setStatus(TransactionStatus.valueOf(dto.getStatus()));
        transaction.setTransactionType(dto.getTransactionType());
        transaction.setReferenceIdentifier(dto.getReferenceIdentifier());
        transaction.setMerchant(MerchantConverter.convertToMerchant(dto.getMerchant()));
        return transaction;
    }


}
