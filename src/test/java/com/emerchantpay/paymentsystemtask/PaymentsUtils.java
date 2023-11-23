package com.emerchantpay.paymentsystemtask;

import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.MerchantStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.model.Merchant;
import com.emerchantpay.paymentsystemtask.model.UserAccount;
import com.emerchantpay.paymentsystemtask.model.transaction.AuthorizeTransaction;
import com.emerchantpay.paymentsystemtask.model.transaction.ChargeTransaction;
import com.emerchantpay.paymentsystemtask.model.transaction.RefundTransaction;
import com.emerchantpay.paymentsystemtask.model.transaction.Transaction;
import com.emerchantpay.paymentsystemtask.response.ResponseMessage;
import com.emerchantpay.paymentsystemtask.utils.TransactionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentsUtils {

    private final Map<String, Transaction> map = new HashMap<>();

    private static Log log = LogFactory.getLog(PaymentsUtils.class);

    public static Transaction setTransactionProperties(TransactionType type, String refId, Merchant merchant) {
        Transaction tr = null;
        switch (type){
          case AUTHORIZE ->{
              tr = new AuthorizeTransaction();
              tr.setTransactionType(type.getName());
          }
          case CHARGE-> {
              tr = new ChargeTransaction();
              tr.setTransactionType(type.getName());
          }
          case REFUND-> {
              tr = new RefundTransaction();
              tr.setTransactionType(type.getName());
          }
      }
        tr.setUuid(TransactionUtils.generateRandomUuid());
        tr.setStatus(TransactionStatus.APPROVED);
        tr.setCustomerEmail("test@test.com");
        tr.setCustomerPhone("+3598841");
        tr.setReferenceIdentifier(refId);
        tr.setMerchant(merchant);
        tr.setAmount(2500.00);
        return tr;
    }

    public static Merchant createMerchant(){
        Merchant merchant = new Merchant("Test1", "test1@test1.com", MerchantStatus.ACTIVE);
        return merchant;
    }

    public static String getTransAsJson(TransactionDto trans){

      List<TransactionDto> transactions = new ArrayList<>();
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

    public static TransactionDto createTransactionDto(String uuid, String status, String type, String referenceId, Double merTotalSum){
        MerchantDto merchant = new MerchantDto("Lidl Germany",
                "lidl@lidl.com",
                MerchantStatus.ACTIVE.getName(),
                merTotalSum);
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


    public static String getMerchantAsJson(List<MerchantDto> merchants){

        ObjectMapper mapper = new ObjectMapper();
        String jsonMer = null;
        try {
            jsonMer = mapper.writeValueAsString(merchants);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return jsonMer;

    }

    public static MerchantDto createMerchantDto() {
        MerchantDto mer = new MerchantDto("Kaufland",
                "kaufland@abv.bg",
                "ACTIVE",
                6000.00);
        mer.setIdentifier(1L);
        return mer;
    }

    public static List<MerchantDto> createListMerchants(){
        MerchantDto merTest1 = new MerchantDto("Test1",
                "test1@test1.com",
                MerchantStatus.ACTIVE.getName(),
                2000.0);
        merTest1.setIdentifier(305L);

        MerchantDto merTest2 = new MerchantDto("Test2",
                "test2@test2.com",
                MerchantStatus.ACTIVE.getName(),
                3000.0);
        merTest2.setIdentifier(306L);

        return List.of(merTest1,merTest2);
    }
    public  static UserAccount createUser(){
        UserAccount user =
                new UserAccount("Test1",
                        "test1@test.com",
                        "MERCHANT",
                        "$2a$10$bH6fi57oRN8dF51apQIiW.ph5nbjwBsYLH71rUNnlDvv4KGI4WDqi");
        return user;
    }

    public static String  getResponseMessageJson(String message){

        ResponseMessage respMessage = new ResponseMessage(message);
        ObjectMapper mapper = new ObjectMapper();
        String jsonMer = null;
        try {
             jsonMer = mapper.writeValueAsString(respMessage);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return jsonMer;
    }
}

