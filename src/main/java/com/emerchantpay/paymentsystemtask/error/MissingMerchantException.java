package com.emerchantpay.paymentsystemtask.error;

public class MissingMerchantException extends Exception {

    public MissingMerchantException(String errorMessage){
        super(errorMessage);
    }


}
