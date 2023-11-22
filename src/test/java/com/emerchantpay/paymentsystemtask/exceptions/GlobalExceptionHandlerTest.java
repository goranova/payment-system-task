package com.emerchantpay.paymentsystemtask.exceptions;

import com.emerchantpay.paymentsystemtask.PaymentsUtils;
import com.emerchantpay.paymentsystemtask.controller.TransactionController;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.service.MerchantService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
@SpringBootTest
public class GlobalExceptionHandlerTest {

    private static final String DUPLICATE_MERCHANT_C = "PAYMENT_SYSTEM.MER_DESCR_STAT_C";

    private static final String RUNTIME_EXCEPTION = "Runtime exception is thrown";
    @Autowired
    private MockMvc mvc;

    @MockBean
    private MerchantService merchantService;

    @MockBean
    private TransactionController transactionController;

    @Test
    public void handleBusinessExceptionTest_merchant() throws Exception {

        Mockito.when(merchantService.processMerchant(Mockito.any()))
                .thenThrow(new MerchantException(Message.INACTIVE_MERCHANT.getName()));
        MvcResult response = mvc.perform(MockMvcRequestBuilders
                        .post("/merchants")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PaymentsUtils.getMerchantAsJson()))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andReturn();

        String responseBody = response.getResponse().getContentAsString();
        assertEquals(responseBody, Message.INACTIVE_MERCHANT.getName());
    }

    @Test
    public void handleBusinessExceptionTest_transaction() throws Exception {

        Mockito.when(transactionController.importTransactions(Mockito.any()))
                .thenThrow(new TransactionException(Message.MISSING_TRANS_REFERENCE_IDENTIFIER.getName()));
        MvcResult response = mvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PaymentsUtils.getTransAsJson()))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andReturn();

        String responseBody = response.getResponse().getContentAsString();
        assertEquals(responseBody, Message.MISSING_TRANS_REFERENCE_IDENTIFIER.getName());
    }

    @Test
    public void handleSqlExceptionTest() throws Exception {

        Mockito.when(merchantService.processMerchant(Mockito.any()))
                .thenThrow(new DataIntegrityViolationException(DUPLICATE_MERCHANT_C));
        MvcResult response = mvc.perform(MockMvcRequestBuilders
                        .post("/merchants")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PaymentsUtils.getMerchantAsJson()))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andReturn();

        String responseBody = response.getResponse().getContentAsString();
        assertEquals(responseBody, Message.DUPLICATE_MERCHANT.getName());
    }

    @Test
    public void handleForbiddenExceptionTest() throws Exception {

        Mockito.when(transactionController.importTransactions(Mockito.any()))
                .thenThrow(HttpClientErrorException
                        .create(HttpStatus.FORBIDDEN, Message.FORBIDDEN.getName(), null, null, null));
        MvcResult response = mvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PaymentsUtils.getTransAsJson()))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();

        String responseBody = response.getResponse().getContentAsString();
        assertEquals(responseBody, Message.FORBIDDEN.getName());
    }

    @Test
    public void handleExceptionTest() throws Exception {

        Mockito.when(transactionController.importTransactions(Mockito.any()))
                .thenThrow(new RuntimeException("Runtime exception is thrown"));
        MvcResult response = mvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PaymentsUtils.getTransAsJson()))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andReturn();

        String responseBody = response.getResponse().getContentAsString();
        assertEquals(responseBody,RUNTIME_EXCEPTION);
    }
}
