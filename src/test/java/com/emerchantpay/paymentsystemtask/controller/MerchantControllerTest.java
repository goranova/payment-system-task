package com.emerchantpay.paymentsystemtask.controller;

import com.emerchantpay.paymentsystemtask.PaymentsUtils;
import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.service.MerchantService;
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

@WebMvcTest(MerchantController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MerchantControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private MerchantService merchantService;

    @Test
    public void findAllMerchantTest() throws Exception {
        List<MerchantDto> merchants = PaymentsUtils.createListMerchants();
        Mockito.when(merchantService.findAllMerchants())
                .thenReturn(merchants);

        MvcResult response = mvc.perform(MockMvcRequestBuilders
                        .get("/merchants/display")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseString = response.getResponse().getContentAsString();

        assertThat(responseString, responseString.contains("ACTIVE"));

    }

    @Test
    public void editMerchantTest() throws Exception {
        MerchantDto existingMerchant =
                PaymentsUtils.createMerchant("Kaufland", "kaufland@abv.bg", "ACTIVE", 6000.00);

        String id = "1";

        Mockito.when(merchantService.findById(id))
                .thenReturn(existingMerchant);

        MvcResult response = mvc.perform(MockMvcRequestBuilders
                        .get("/merchants/edit/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseString = response.getResponse().getContentAsString();

        assertThat(responseString, responseString.contains("Choose Status:"));

    }


    @Test
    public void saveMerchantTestSuccess() throws Exception {
        MerchantDto existingMerchant =
                PaymentsUtils.createMerchant("Kaufland", "kaufland@abv.bg", "ACTIVE", 6000.00);

        Mockito.when(merchantService.editMerchant(Mockito.any(), Mockito.any()))
                .thenReturn(true);

        MvcResult response = mvc.perform(MockMvcRequestBuilders
                        .post("/merchants/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .sessionAttr("existingMerchant", existingMerchant))
                .andDo(print())
                .andExpect(status().isFound())
                .andReturn();


        String responseHeader = response.getResponse().getHeader("Location");

        assertThat(responseHeader, responseHeader.equals("/alerts/successAlert"));
    }

    @Test
    public void saveMerchantTestFail() throws Exception {
        MerchantDto existingMerchant =
                PaymentsUtils.createMerchant("Kaufland", "kaufland@abv.bg", "ACTIVE", 6000.00);


        Mockito.when(merchantService.editMerchant(Mockito.any(), Mockito.any()))
                .thenReturn(false);

        MvcResult response = mvc.perform(MockMvcRequestBuilders
                        .post("/merchants/save")
                        .accept(MediaType.APPLICATION_JSON)
                        .sessionAttr("existingMerchant", existingMerchant))
                .andDo(print())
                .andExpect(status().isFound())
                .andReturn();

        String responseHeader = response.getResponse().getHeader("Location");

        assertThat(responseHeader, responseHeader.equals("/alerts/errorAlert"));
    }


    @Test
    public void deleteMerchantTestSuccess() throws Exception {
        MerchantDto existingMerchant =
                PaymentsUtils.createMerchant("Kaufland", "kaufland@abv.bg", "ACTIVE", 6000.00);

        String id = "1";
        Mockito.when(merchantService.findById(id))
                .thenReturn(existingMerchant);
        Mockito.doNothing().when(merchantService).deleteMerchant(existingMerchant);


        MvcResult response = mvc.perform(MockMvcRequestBuilders
                        .post("/merchants/delete/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andReturn();

        String responseHeader = response.getResponse().getHeader("Location");

        assertThat(responseHeader, responseHeader.equals("/alerts/successAlert"));

    }

    @Test
    public void deleteMerchantTestFail() throws Exception {
        MerchantDto existingMerchant =
                PaymentsUtils.createMerchant("Kaufland", "kaufland@abv.bg", "ACTIVE", 6000.00);
        TransactionDto trans = new TransactionDto();
        existingMerchant.setTransactions(List.of(trans));

        String id = "1";
        Mockito.when(merchantService.findById(id))
                .thenReturn(existingMerchant);
        Mockito.doNothing().when(merchantService).deleteMerchant(existingMerchant);


        MvcResult response = mvc.perform(MockMvcRequestBuilders
                        .post("/merchants/delete/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andReturn();

        String responseHeader = response.getResponse().getHeader("Location");

        assertThat(responseHeader, responseHeader.equals("/alerts/errorAlert"));

    }

    @Test
    public void importMerchantTest() throws Exception {
        List<MerchantDto> merchants = PaymentsUtils.createListMerchants();

        Mockito.when(merchantService.saveMerchants(Mockito.any()))
                .thenReturn(merchants);

        MvcResult response = mvc.perform(MockMvcRequestBuilders
                        .post("/merchants/importMerchants")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(PaymentsUtils.getMerchantAsJson()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseString = response.getResponse().getContentAsString();

        assertThat(responseString, responseString.contains("\"description\":\"Lidl\""));

    }
}
