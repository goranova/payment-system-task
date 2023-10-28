package com.emerchantpay.paymentsystemtask.dto;

import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.model.*;
import org.hibernate.dialect.unique.CreateTableUniqueDelegate;
import org.hibernate.engine.spi.ManagedEntity;

public class TransactionConverter {

    public static TransactionDto convertToTransactionDto(AuthorizeTransaction authorizeTransaction) {
        TransactionDto dto=new TransactionDto();
        dto.setUuid(authorizeTransaction.getUuid());
        dto.setStatus(authorizeTransaction.getStatus().name());
        dto.setTransactionType(authorizeTransaction.getTransactionType());
        dto.setCustomerEmail(authorizeTransaction.getCustomerEmail());
        dto.setCustomerPhone(authorizeTransaction.getCustomerPhone());
        dto.setReferenceIdentifier(authorizeTransaction.getReferenceIdentifier());
        dto.setAmount(authorizeTransaction.getAmount());
        dto.setMerchant(authorizeTransaction.getMerchant());
        return dto;
    }

    public static TransactionDto convertToTransactionDto(ChargeTransaction chargeTransaction) {
        TransactionDto dto=new TransactionDto();
        dto.setUuid(chargeTransaction.getUuid());
        dto.setStatus(chargeTransaction.getStatus().name());
        dto.setTransactionType(chargeTransaction.getTransactionType());
        dto.setCustomerEmail(chargeTransaction.getCustomerEmail());
        dto.setCustomerPhone(chargeTransaction.getCustomerPhone());
        dto.setReferenceIdentifier(chargeTransaction.getReferenceIdentifier());
        dto.setAmount(chargeTransaction.getAmount());
        dto.setMerchant(chargeTransaction.getMerchant());
        return dto;
    }

    public static TransactionDto convertToTransactionDto(RefundTransaction refundTransaction) {
        TransactionDto dto=new TransactionDto();
        dto.setUuid(refundTransaction.getUuid());
        dto.setStatus(refundTransaction.getStatus().name());
        dto.setTransactionType(refundTransaction.getTransactionType());
        dto.setCustomerEmail(refundTransaction.getCustomerEmail());
        dto.setCustomerPhone(refundTransaction.getCustomerPhone());
        dto.setReferenceIdentifier(refundTransaction.getReferenceIdentifier());
        dto.setAmount(refundTransaction.getAmount());
        dto.setMerchant(refundTransaction.getMerchant());
        return dto;

    }

    public static TransactionDto convertToTransactionDto(ReversalTransaction reversalTransaction) {
        TransactionDto dto=new TransactionDto();
        dto.setUuid(reversalTransaction.getUuid());
        dto.setStatus(reversalTransaction.getStatus().name());
        dto.setTransactionType(reversalTransaction.getTransactionType());
        dto.setCustomerEmail(reversalTransaction.getCustomerEmail());
        dto.setCustomerPhone(reversalTransaction.getCustomerPhone());
        dto.setReferenceIdentifier(reversalTransaction.getReferenceIdentifier());
        dto.setMerchant(reversalTransaction.getMerchant());
        return dto;

    }

    public static AuthorizeTransaction convertToAuthorize(TransactionDto dto){
        AuthorizeTransaction auth = new AuthorizeTransaction();
        auth.setUuid(dto.getUuid());
        auth.setAmount(dto.getAmount());
        auth.setCustomerEmail(dto.getCustomerEmail());
        auth.setCustomerPhone(dto.getCustomerPhone());
        auth.setStatus(TransactionStatus.valueOf(dto.getStatus()));
        auth.setTransactionType(dto.getTransactionType());
        auth.setMerchant(dto.getMerchant());
        auth.setReferenceIdentifier(dto.getReferenceIdentifier());
        return auth;
    }

    public static ChargeTransaction convertToCharge(TransactionDto dto){
        ChargeTransaction charge = new ChargeTransaction();
        charge.setUuid(dto.getUuid());
        charge.setAmount(dto.getAmount());
        charge.setCustomerEmail(dto.getCustomerEmail());
        charge.setCustomerPhone(dto.getCustomerPhone());
        charge.setStatus(TransactionStatus.valueOf(dto.getStatus()));
        charge.setTransactionType(dto.getTransactionType());
        charge.setReferenceIdentifier(dto.getReferenceIdentifier());
        charge.setMerchant(dto.getMerchant());

        return charge;
    }

    public static RefundTransaction convertToRefund(TransactionDto dto){
        RefundTransaction refund = new RefundTransaction();
        refund.setUuid(dto.getUuid());
        refund.setAmount(dto.getAmount());
        refund.setCustomerEmail(dto.getCustomerEmail());
        refund.setCustomerPhone(dto.getCustomerPhone());
        refund.setStatus(TransactionStatus.valueOf(dto.getStatus()));
        refund.setTransactionType(dto.getTransactionType());
        refund.setReferenceIdentifier(dto.getReferenceIdentifier());
        refund.setMerchant(dto.getMerchant());

        return refund;
    }

    public static ReversalTransaction convertToReversal(TransactionDto dto){
        ReversalTransaction reversal = new ReversalTransaction();
        reversal.setUuid(dto.getUuid());
        reversal.setCustomerEmail(dto.getCustomerEmail());
        reversal.setCustomerPhone(dto.getCustomerPhone());
        reversal.setStatus(TransactionStatus.valueOf(dto.getStatus()));
        reversal.setTransactionType(dto.getTransactionType());
        reversal.setReferenceIdentifier(dto.getReferenceIdentifier());
        reversal.setMerchant(dto.getMerchant());
        return reversal;
    }


}
