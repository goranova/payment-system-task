package com.emerchantpay.paymentsystemtask.enums;

public enum TransactionStatus {
    APPROVED("APPROVED"),
    REVERSED("REVERSED"),
    REFUNDED("REFUNDED"),
    ERROR("ERROR");

    private String value;
    TransactionStatus(String value){
        this.value=value;
    }

}
