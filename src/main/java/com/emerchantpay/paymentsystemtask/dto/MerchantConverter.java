package com.emerchantpay.paymentsystemtask.dto;

import com.emerchantpay.paymentsystemtask.enums.MerchantStatus;
import com.emerchantpay.paymentsystemtask.model.Merchant;
import com.emerchantpay.paymentsystemtask.model.transaction.Transaction;

import java.util.Set;

public class MerchantConverter {

    public static MerchantDto convertToMerchantDto(Merchant merchant) {
        MerchantDto merchantDto = new MerchantDto();
        merchantDto.setDescription(merchant.getDescription());
        merchantDto.setEmail(merchant.getEmail());
        merchantDto.setMerchantStatus(merchant.getStatus().getName());
        merchantDto.setIdentifier(merchant.getIdentifier());
        merchantDto.setTotalTransactionSum(merchant.getTotalTransactionSum());

        return merchantDto;
    }

    public static Merchant convertToMerchant(MerchantDto merchantDto) {
        Merchant merchant = new Merchant();
        merchant.setIdentifier(merchantDto.getIdentifier());
        merchant.setDescription(merchantDto.getDescription());
        merchant.setEmail(merchantDto.getEmail());
        merchant.setStatus(MerchantStatus.valueOf(merchantDto.getMerchantStatus()));
        merchant.setTotalTransactionSum(merchantDto.getTotalTransactionSum());
        return merchant;
    }

    public static MerchantDto convertToMerchantWithTransactionsDto(Merchant merchant, Set<Transaction> transactions){

        MerchantDto convertedMerchant = convertToMerchantDto(merchant);
        Set<TransactionDto> convertedTrans = TransactionConverter.convertToTransactionDto(transactions);
        convertedMerchant.setTransactions(convertedTrans);
        return convertedMerchant;
    }
}
