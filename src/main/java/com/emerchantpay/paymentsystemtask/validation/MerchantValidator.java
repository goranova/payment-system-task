package com.emerchantpay.paymentsystemtask.validation;

import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.enums.MerchantStatus;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.exceptions.MerchantException;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.stream.Stream;


public class MerchantValidator {

    public MerchantDto validate(MerchantDto merchantDto) throws MerchantException {

        if(merchantDto==null){
            throw new MerchantException(Message.MISSING_MERCHANT.getName());
        }
        validateSupportedStatus(merchantDto);
        validateStatus(merchantDto);
        validateTotalTrSum(merchantDto);
        validateEmail(merchantDto);
        validateDescription(merchantDto);
        return merchantDto;
    }

    public MerchantDto validateSupportedStatus(MerchantDto merchant) throws MerchantException {
        boolean isStatusNonMatch = Stream.of(MerchantStatus.values())
                .noneMatch(s->s.getName().equals(merchant.getMerchantStatus()));

        if(merchant.getMerchantStatus()==null || isStatusNonMatch){
            throw new MerchantException(Message.UNSUPPORTED_MERCHANT_STATUS.getName(),
                    merchant.getMerchantStatus());
        }
        return merchant;
    }

    public MerchantDto validateStatus(MerchantDto merchantDto) throws MerchantException {

        if(merchantDto.getMerchantStatus().equals(MerchantStatus.INACTIVE.name() )
                && !merchantDto.getTransactions().isEmpty()) {
            throw new MerchantException(Message.INACTIVE_MERCHANT.getName());
        }
        return merchantDto;
    }

    public MerchantDto validateEmail(MerchantDto merchantDto) throws MerchantException {

        boolean isValid = EmailValidator.getInstance().isValid(merchantDto.getEmail());
        if (!isValid) {
            throw new MerchantException(Message.INVALID_EMAIL.getName(), merchantDto.getEmail());
        }
        return merchantDto;

    }

    public MerchantDto validateTotalTrSum(MerchantDto merchantDto) throws MerchantException {

        if (merchantDto.getTotalTransactionSum() == null || merchantDto.getTotalTransactionSum() < 0) {
            throw new MerchantException(Message.INVALID_TRANSACTION_SUM.getName(), String.valueOf(merchantDto.getTotalTransactionSum()));
        }
        return merchantDto;
    }

    public MerchantDto validateDescription (MerchantDto merchant) throws MerchantException {
        if(merchant.getDescription()==null){
            throw new MerchantException(Message.MISSING_MERCHANT_DESCRIPTION.getName());
        }
        return merchant;
    }
}