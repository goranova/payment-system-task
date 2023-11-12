package com.emerchantpay.paymentsystemtask.exceptions;

public class TransactionException extends Exception {

    public  TransactionException (String errorMessage, String... value){
        super(String.format(errorMessage, value));
    }

}
