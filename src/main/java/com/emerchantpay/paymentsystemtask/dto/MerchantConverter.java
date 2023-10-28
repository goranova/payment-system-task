package com.emerchantpay.paymentsystemtask.dto;

import com.emerchantpay.paymentsystemtask.enums.MerchantStatus;
import com.emerchantpay.paymentsystemtask.model.Merchant;

import java.util.stream.Collectors;

public class MerchantConverter {

    public static MerchantDto convertToMerchantDto(Merchant merchant) {
        MerchantDto merchantDto = new MerchantDto();
        merchantDto.setDescription(merchant.getDescription());
        merchantDto.setEmail(merchant.getEmail());
        merchantDto.setMerchantStatus(merchant.getStatus().name());
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
}
