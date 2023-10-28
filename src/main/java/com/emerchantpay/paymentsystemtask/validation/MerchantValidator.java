package com.emerchantpay.paymentsystemtask.validation;

import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.enums.MerchantStatus;
import org.apache.commons.validator.routines.EmailValidator;

public class MerchantValidator {


    public MerchantDto validate(MerchantDto merchantDto) {
        validateEmail(merchantDto);
        validateStatus(merchantDto);
        validateTotalTrSum(merchantDto);
        return merchantDto;
    }

    public MerchantDto validateStatus(MerchantDto merchantDto) {

        if (merchantDto.getMerchantStatus() != null &&
                (merchantDto.getMerchantStatus().equals(MerchantStatus.ACTIVE.name())
                        || merchantDto.getMerchantStatus().equals(MerchantStatus.INACTIVE.name()))) {

            merchantDto.setMerchantStatus(MerchantStatus.INACTIVE.name());
        }
        return merchantDto;


    }

    public MerchantDto validateEmail(MerchantDto merchantDto) {
        boolean isValid = EmailValidator.getInstance().isValid(merchantDto.getEmail());
        if (isValid) {
            return merchantDto;
        }
        merchantDto.setMerchantStatus(MerchantStatus.INACTIVE.name());
        return merchantDto;

    }

    public MerchantDto validateTotalTrSum(MerchantDto merchantDto) {
        if (merchantDto.getTotalTransactionSum() != null && merchantDto.getTotalTransactionSum() > 0) {
            return merchantDto;
        }
        merchantDto.setMerchantStatus(MerchantStatus.INACTIVE.name());
        return merchantDto;
    }

}
