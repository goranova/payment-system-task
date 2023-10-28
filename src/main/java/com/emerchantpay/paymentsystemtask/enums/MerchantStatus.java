package com.emerchantpay.paymentsystemtask.enums;

import com.emerchantpay.paymentsystemtask.model.Merchant;

public enum MerchantStatus {

    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private String value;
    MerchantStatus (String value){
        this.value=value;
    }
}
