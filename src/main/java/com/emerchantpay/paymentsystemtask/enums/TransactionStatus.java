package com.emerchantpay.paymentsystemtask.enums;

public enum TransactionStatus {
    APPROVED("APPROVED"),
    REVERSED("REVERSED"),
    REFUNDED("REFUNDED"),
    ERROR("ERROR");

    private String name;
    TransactionStatus(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }

}
