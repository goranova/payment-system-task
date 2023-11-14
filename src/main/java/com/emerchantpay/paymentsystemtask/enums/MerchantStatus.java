package com.emerchantpay.paymentsystemtask.enums;

public enum MerchantStatus {

    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private String name;
    MerchantStatus (String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
}
