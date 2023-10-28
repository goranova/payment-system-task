package com.emerchantpay.paymentsystemtask.enums;

import java.util.Arrays;
import java.util.Optional;

public enum TransactionType {
    AUTHORIZE("Authorize"),
    CHARGE("Charge"),

    REFUND("Refund"),

    REVERSAL("Reversal");

    private String name;

    TransactionType (String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public static Optional<TransactionType> getByValue(String value) {
        return Arrays.stream(TransactionType.values())
                .filter(trans -> trans.getName().equalsIgnoreCase(value)).findFirst();
    }



}
