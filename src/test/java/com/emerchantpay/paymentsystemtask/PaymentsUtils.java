package com.emerchantpay.paymentsystemtask;

import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.MerchantStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.model.Merchant;
import com.emerchantpay.paymentsystemtask.model.UserAccount;
import com.emerchantpay.paymentsystemtask.model.transaction.*;
import com.emerchantpay.paymentsystemtask.utils.TransactionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import java.util.*;

public class PaymentsUtils {

    private final Map<String, Transaction> map = new HashMap<>();

    private static Log log = LogFactory.getLog(PaymentsUtils.class);


    public PaymentsUtils() {
        map.put(TransactionType.AUTHORIZE.getName(), new AuthorizeTransaction());
        map.put(TransactionType.CHARGE.getName(), new ChargeTransaction());
        map.put(TransactionType.REFUND.getName(), new RefundTransaction());
        map.put(TransactionType.REVERSAL.getName(), new ReversalTransaction());
    }

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

    public static Set<TransactionDto> createSetTransactions(){
        TransactionDto auth = new TransactionDto();
        auth.setStatus("APPROVED");
        auth.setTransactionType("Authorize");
        auth.setAmount(200.00);
        return  Set.of(auth);
    }


    public  Transaction createTransaction(String type, String referenceId, Merchant merchant) {

        Transaction transaction = map.get(type);
        transaction.setUuid(TransactionUtils.generateRandomUuid());
        transaction.setStatus(TransactionStatus.APPROVED);
        transaction.setTransactionType(type);
        transaction.setCustomerEmail("test@test.com");
        transaction.setAmount(2500.00);
        transaction.setReferenceIdentifier(referenceId);
        transaction.setMerchant(merchant);
        return transaction;
    }

    public static Merchant createMerchant(){
        Merchant merchant = new Merchant("Test1", "test1@test1.com", MerchantStatus.ACTIVE);
        return merchant;
    }


    public  static UserAccount createUser(){
        UserAccount user =
                new UserAccount("Test1",
                                   "test1@test.com",
                                    "MERCHANT",
                                 "$2a$10$bH6fi57oRN8dF51apQIiW.ph5nbjwBsYLH71rUNnlDvv4KGI4WDqi");
        return user;
    }

    public static String getTransAsJson(String uuid, String type, String referenceId){

      List<TransactionDto> transactions = new ArrayList<>();
      MerchantDto merchant = new MerchantDto("Lidl Germany", "lidl@lidl.com",MerchantStatus.ACTIVE.getName(),2500.00);
      merchant.setIdentifier(350L);
      TransactionDto trans =
                new TransactionDto(uuid,TransactionStatus.APPROVED.getName(),
                        type,
                        "tsvety@gmail.com",
                        "+35988417",
                        500.00,
                        referenceId,merchant);

        transactions.add(trans);
        ObjectMapper mapper = new ObjectMapper();
        String jsonTrans = null;
        try {
            jsonTrans = mapper.writeValueAsString(transactions);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return jsonTrans;
    }

    public static TransactionDto createTransactionDto(String uuid, String status, String type, String referenceId){
        MerchantDto merchant = new MerchantDto("Lidl Germany", "lidl@lidl.com",MerchantStatus.ACTIVE.getName(),24350.00);
        merchant.setIdentifier(350L);
        TransactionDto trans =
                new TransactionDto(uuid,status,
                        type,
                        "tsvety@gmail.com",
                        "+35988417",
                        500.00,
                        referenceId,merchant);
        return trans;
    }

    public static String getTransAsJson() {
        List<TransactionDto> transactions = new ArrayList<>();
        TransactionDto trans = new TransactionDto("072C5DDA-0AF8-42EA-ACEB-068C52C2C3AC",
                TransactionStatus.APPROVED.getName(),
                TransactionType.AUTHORIZE.getName(),
                "tsvety@gmail.com",
                "+35988417",
                2000.00, null,null);

        transactions.add(trans);

        ObjectMapper mapper = new ObjectMapper();
        String jsonTrans = null;
        try {
            jsonTrans = mapper.writeValueAsString(transactions);

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        return jsonTrans;
    }



    public static String getMerchantAsJson(){
        return "[{\"description\": \"Carrefour\",\n" +
                "\t\"email\": \"Carrefour@gmail.com\",\n" +
                "\t\"merchantStatus\": \"INACTIVE\",\n" +
                "\t\"totalTransactionSum\": \"2500\"}]";
    }
}

