package com.emerchantpay.paymentsystemtask.enums;

import java.util.Locale;
import java.util.ResourceBundle;


public enum Message {


    MISSING_MERCHANT("missing.merchant"),

    INACTIVE_MERCHANT("inactive.merchant"),

    UNSUPPORTED_MERCHANT_STATUS("unsupported.merchant.status"),

    MISSING_MERCHANT_DESCRIPTION("missing.mer.description"),

    INVALID_EMAIL("invalid.email"),

    INVALID_TRANSACTION_SUM("invalid.transaction.sum"),

    UNSUPPORTED_TRANSACTION_STATUS("unsupported.trans.status"),

    UNSUPPORTED_TRANSACTION_TYPE("unsupported.transaction.type"),

    MISSING_TRANSACTION("missing.transaction"),

    MISSING_TRANS_REFERENCE_IDENTIFIER("missing.trans.reference.identifier"),

    MULTIPLE_TRANSACTIONS("multiple.trans"),

    DUPLICATE_MERCHANT("duplicate.merchant"),

    EDIT_MERCHANT("edit.merchant"),

    DELETE_MERCHANT("delete.merchant"),

    COULD_NOT_UPLOAD_FILE("could.not.upload.file"),

    UPLOAD_CSV_FILE("upload.csv.file"),

    UPLOAD_FILE_SUCCESSFULLY("upload.file.successfully");


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
