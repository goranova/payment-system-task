package com.emerchantpay.paymentsystemtask.controller;

import com.emerchantpay.paymentsystemtask.PaymentsUtils;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.response.ResponseTransactionMessage;
import com.emerchantpay.paymentsystemtask.service.TransactionService;
import com.emerchantpay.paymentsystemtask.service.handler.TransactionHandlerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TransactionControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    TransactionHandlerService transactionHandlerService;

    @MockBean
    TransactionService transactionService;

    @Test
    public void findTransactionTest() throws Exception {

        TransactionDto auth = new TransactionDto();
        auth.setTransactionType(TransactionType.AUTHORIZE.getName());
        auth.setStatus(TransactionStatus.APPROVED.getName());
        auth.setAmount(200.0);

        Mockito.when(transactionService.findAuthenticatedUserTransactions())
                .thenReturn(Set.of(auth));

        File transHtmlResponse = new File("src/test/resources/transactionResponse.html");
        String transResponse = new String(Files.readAllBytes(transHtmlResponse.toPath()));

        mvc.perform(MockMvcRequestBuilders
                        .get("/transactions")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(transResponse))
                .andReturn();
    }

    @Test
    public void importAuthTransactionsTest() throws Exception {

        TransactionDto auth = PaymentsUtils.createTransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf53",
                                                                  TransactionStatus.APPROVED.getName(),
                                                                  TransactionType.AUTHORIZE.getName(),
                                                         null,24350.0);
        String content = PaymentsUtils.getTransAsJson(auth);


        ResponseTransactionMessage message =
                new ResponseTransactionMessage(Message.TRANSACTION_SAVED_SUCCESS.getName(), List.of(auth));

        Mockito.when(transactionHandlerService.handleTransactions(Mockito.any()))
                .thenReturn(List.of(message));

        File transFileResponse = new File("src/test/resources/AuthResponse.json");
        String transResponse = new String(Files.readAllBytes(transFileResponse.toPath()));
        mvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(transResponse))
                .andReturn();
    }

    @Test
    public void importReversalErrorTransactionsTest() throws Exception {

        TransactionDto reversal = PaymentsUtils.createTransactionDto ("a404d5c4-1b49-40fa-9d0e-d36e500bdf52",
                                  TransactionStatus.APPROVED.getName(),
                                  TransactionType.REVERSAL.getName(),
                         "a404d5c4-1b49-40fa-9d0e-d36e500bdf49",24350.0);

        String content = PaymentsUtils.getTransAsJson(reversal);

        TransactionDto reversalError = PaymentsUtils.createTransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf52",
                TransactionStatus.ERROR.getName(),
                TransactionType.REVERSAL.getName(),
                null,24350.0);

        ResponseTransactionMessage message =
                new ResponseTransactionMessage(String.format(Message.TRANSACTION_REFERENCE.getName(),
                        TransactionType.AUTHORIZE.getName(),
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf49"),List.of(reversalError));

        Mockito.when(transactionHandlerService.handleTransactions(Mockito.any()))
                .thenReturn(List.of(message));

        File transFileResponse = new File("src/test/resources/ReversalErrorResponse.json");
        String transResponse = new String(Files.readAllBytes(transFileResponse.toPath()));
        mvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(transResponse))
                .andReturn();
    }

    @Test
    public void importRefundTransactionsTest() throws Exception {

        TransactionDto chTrans = PaymentsUtils.createTransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf54",
                TransactionStatus.REFUNDED.getName(),
                TransactionType.CHARGE.getName(),
                "a404d5c4-1b49-40fa-9d0e-d36e500bdf53",24850.0);

        TransactionDto refTrans = PaymentsUtils.createTransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf55",
                TransactionStatus.APPROVED.getName(),
                TransactionType.REFUND.getName(),
                "a404d5c4-1b49-40fa-9d0e-d36e500bdf53",24350.0);

        String content = PaymentsUtils.getTransAsJson(refTrans);
        ResponseTransactionMessage message =
                new ResponseTransactionMessage(String.format(Message.TRANSACTION_SAVED_SUCCESS.getName()),List.of(chTrans,refTrans));

        Mockito.when(transactionHandlerService.handleTransactions(Mockito.any()))
                .thenReturn(List.of(message));

        File transFileResponse = new File("src/test/resources/RefundResponse.json");
        String transResponse = new String(Files.readAllBytes(transFileResponse.toPath()));
        mvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(transResponse))
                .andReturn();
    }
}
