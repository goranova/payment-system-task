package com.emerchantpay.paymentsystemtask.exceptions;

public class MerchantException extends Exception {

    public MerchantException(String errorMessage){
        super(errorMessage);
    }

    public  MerchantException (String errorMessage, String value){
        super(String.format(errorMessage, value));
    }


}
