package com.emerchantpay.paymentsystemtask.utils;

import java.util.UUID;

public class TransactionUtils {

    public static String generateRandomUuid(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
