package com.emerchantpay.paymentsystemtask.controller;

import com.emerchantpay.paymentsystemtask.PaymentsUtils;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.service.handler.TransactionHandlerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TransactionControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    TransactionHandlerService transactionService;

    @Test
    public void findAllTransactionTest() throws Exception {
        List<TransactionDto> transactions = PaymentsUtils.createTransaction();
        Mockito.when(transactionService.getAllTransactions())
                .thenReturn(transactions);

        MvcResult response = mvc.perform(MockMvcRequestBuilders
                        .get("/transaction/display")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseString = response.getResponse().getContentAsString();

        assertThat(responseString, responseString.contains("APPROVED"));

    }

    @Test
    public void importTransactionsTest() throws Exception {
        List<TransactionDto> transactions = PaymentsUtils.createTransaction();

        Mockito.when(transactionService.handleTransactionChain(Mockito.any()))
                .thenReturn(transactions);

        MvcResult response = mvc.perform(MockMvcRequestBuilders
                        .post("/transaction/importTransaction")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(PaymentsUtils.getTransAsJson()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseString = response.getResponse().getContentAsString();

        assertThat(responseString, responseString.contains("\"status\":\"APPROVED\""));

    }
}
