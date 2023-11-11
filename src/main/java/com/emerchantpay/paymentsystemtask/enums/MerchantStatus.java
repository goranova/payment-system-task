package com.emerchantpay.paymentsystemtask.enums;

public enum MerchantStatus {

    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private String value;
    MerchantStatus (String value){
        this.value=value;
    }
}
