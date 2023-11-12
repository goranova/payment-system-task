package com.emerchantpay.paymentsystemtask.enums;

import java.util.Locale;
import java.util.ResourceBundle;


public enum Message {


    MISSING_MERCHANT("missing.merchant"),

    INACTIVE_MERCHANT("inactive.merchant"),

    INVALID_EMAIL("invalid.email"),

    INVALID_TRANSACTION_SUM("invalid.transaction.sum"),

    INVALID_TRANSACTION_STATUS("invalid.trans.status"),

    UNSUPPORTED_TRANSACTION_TYPE("unsupported.transaction.type");

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("messages",
            Locale.ENGLISH);

    private String name;
    Message(String name){
        this.name=name;
    }
    public String getName(){
        return resourceBundle.getString(name);
    }
}
