package com.emerchantpay.paymentsystemtask;

import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;

import java.util.List;

public class PaymentsUtils {

    public static List<MerchantDto> createListMerchants() {
        MerchantDto merchantDto = createMerchant("Lidl", "lidl@gmail.com", "ACTIVE", 2000.00);
        return List.of(merchantDto);
    }

    public static MerchantDto createMerchant(String descr, String email, String status, Double sum) {
        MerchantDto merchantDto = new MerchantDto();
        merchantDto.setDescription(descr);
        merchantDto.setMerchantStatus(status);
        merchantDto.setEmail(email);
        merchantDto.setTotalTransactionSum(sum);
        return merchantDto;
    }


    public static String getMerchantAsJson(){
        return "[{\"description\": \"Carrefour\",\n" +
                "\t\"email\": \"Carrefour@gmail.com\",\n" +
                "\t\"merchantStatus\": \"ACTIVE\",\n" +
                "\t\"totalTransactionSum\": \"2500\"}]";

    }

    public static List<TransactionDto>  createTransaction(){
        TransactionDto auth = new TransactionDto();
        auth.setStatus("APPROVED");
        auth.setTransactionType("Authorize");
        auth.setAmount(200.00);
        return  List.of(auth);
    }

    public static String getTransAsJson(){
        return "[\n" +
                "{\n" +
                " \"uuid\": \"072C5DDA-0AF8-42EA-ACEB-068C52C2C3AC\",\n" +
                " \"status\": \"APPROVED\",\n" +
                "  \"transactionType\": \"Authorize\",\n" +
                "  \"customerEmail\": \"tsvety@gmail.com\",\n" +
                "  \"customerPhone\": \"+35988417\",\n" +
                "  \"amount\": \"2000.0\"\n" +
                "\t}\n" +
                "]";

    }
}

