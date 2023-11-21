package com.emerchantpay.paymentsystemtask.exceptions;

import com.emerchantpay.paymentsystemtask.PaymentsUtils;
import com.emerchantpay.paymentsystemtask.controller.MerchantController;
import com.emerchantpay.paymentsystemtask.enums.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
@SpringBootTest
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MerchantController merchantController;

    @Test
    public void handleBusinessExceptionTest() throws Exception {

        Mockito.when(merchantController.importMerchants(Mockito.any()))
                .thenThrow(new MerchantException(Message.MISSING_MERCHANT.getName()));
       MvcResult response = mvc.perform(MockMvcRequestBuilders
                        .post("/merchants")
                        .accept(MediaType.APPLICATION_JSON)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(PaymentsUtils.getMerchantAsJson()))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andReturn();

        String responseHeader = response.getResponse().getHeader("Location");
    }




}
