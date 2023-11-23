package com.emerchantpay.paymentsystemtask.controller;

import com.emerchantpay.paymentsystemtask.PaymentsUtils;
import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.MerchantStatus;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.service.MerchantService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        File merHtmlResponse = new File("src/test/resources/merchantResponse.html");
        String merResponse = new String(Files.readAllBytes(merHtmlResponse.toPath()));

        mvc.perform(MockMvcRequestBuilders
                        .get("/merchants")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(merResponse))
                .andReturn();
    }

    @Test
    public void editMerchantTest() throws Exception {

        MerchantDto existingMerchant = PaymentsUtils.createMerchantDto();
        String id = "1";

        Mockito.when(merchantService.findById(id))
                .thenReturn(existingMerchant);

        File merHtmlResponse = new File("src/test/resources/merchantEditResponse.html");
        String merResponse = new String(Files.readAllBytes(merHtmlResponse.toPath()));

        mvc.perform(MockMvcRequestBuilders
                        .get("/merchants/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(merResponse))
                .andReturn();
    }


    @Test
    public void updateMerchantTest_success() throws Exception {

        MerchantDto existingMerchant = PaymentsUtils.createMerchantDto();
        String id = "1";

        Mockito.when(merchantService.findById(id))
                .thenReturn(existingMerchant);
        Mockito.when(merchantService.editMerchant(Mockito.any(),Mockito.any()))
                .thenReturn(true);

        mvc.perform(MockMvcRequestBuilders
                        .put("/merchants/{id}",id)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/alerts/successAlert"))
                .andReturn();
    }

    @Test
    public void updateMerchantTest_fail() throws Exception {

        MerchantDto existingMerchant = PaymentsUtils.createMerchantDto();
        String id = "1";

        Mockito.when(merchantService.findById(id))
                .thenReturn(existingMerchant);
        Mockito.when(merchantService.editMerchant(Mockito.any(),Mockito.any()))
                .thenReturn(false);

        mvc.perform(MockMvcRequestBuilders
                        .put("/merchants/{id}",id)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/alerts/errorAlert"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", Message.EDIT_MERCHANT.getName()))
                .andReturn();
    }


    @Test
    public void deleteMerchantTest_success() throws Exception {

       MerchantDto existingMerchant = PaymentsUtils.createMerchantDto();
        String id = "1";

        Mockito.when(merchantService.findById(id))
                .thenReturn(existingMerchant);
        Mockito.doNothing().when(merchantService).deleteMerchant(existingMerchant);

        mvc.perform(MockMvcRequestBuilders
                        .delete("/merchants/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/alerts/successAlert"))
                .andReturn();
    }

    @Test
    public void deleteMerchantTest_fail() throws Exception {

        MerchantDto existingMerchant = PaymentsUtils.createMerchantDto();
        String id = "1";
        TransactionDto tr = new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf53",
                TransactionStatus.APPROVED.getName(),
                TransactionType.AUTHORIZE.getName(),
                null,null,
                250.0,null,existingMerchant);
        existingMerchant.setTransactions(Set.of(tr));

        Mockito.when(merchantService.findById(id))
                .thenReturn(existingMerchant);
        Mockito.doNothing().when(merchantService).deleteMerchant(existingMerchant);

        mvc.perform(MockMvcRequestBuilders
                        .delete("/merchants/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/alerts/errorAlert"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", Message.DELETE_MERCHANT.getName()))
                .andReturn();

    }

    @Test
    public void importMerchantTest() throws Exception {

        MerchantDto mer0 = new MerchantDto("Test1",
                "test1@test1.com",
                MerchantStatus.ACTIVE.getName(),
                2000.0) ;
        MerchantDto mer1 =  new MerchantDto("Test2",
                "test2@test2.com",
                MerchantStatus.ACTIVE.getName(),
                3000.0);
        String content = PaymentsUtils.getMerchantAsJson(List.of(mer0,mer1));
        mer0.setIdentifier(305L);
        mer1.setIdentifier(306L);

        Mockito.when(merchantService.processMerchant(Mockito.any()))
                .thenReturn(mer0, mer1);


        File merFileResponse = new File("src/test/resources/merchantImportResponse.json");
        String merResponse = new String(Files.readAllBytes(merFileResponse.toPath()));

        mvc.perform(MockMvcRequestBuilders
                        .post("/merchants")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(merResponse))
                .andReturn();
    }

    @Test
    public void uploadFileTest_success() throws Exception {

        byte[] fileBytes = Files.readAllBytes(Paths.get("src/test/resources/merchants.csv"));
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                "merchants.csv",
                "text/csv",
                fileBytes);
        String jsonMer = PaymentsUtils.getResponseMessageJson(String.format(Message.UPLOAD_FILE_SUCCESSFULLY.getName(),
                                                                            multipartFile.getOriginalFilename()));
        Mockito.doNothing().when(merchantService).save(multipartFile);

        mvc.perform(MockMvcRequestBuilders.multipart("/merchants/files")
                        .file(multipartFile)
                        .contentType("text/csv;charset=UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(jsonMer))
                .andReturn();

    }

    @Test
    public void uploadFileTest_fail() throws Exception {

        byte[] fileBytes = Files.readAllBytes(Paths.get("src/test/resources/merchants.txt"));
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                "merchants.txt",
                MediaType.TEXT_PLAIN_VALUE,
                fileBytes);
        String jsonMer = PaymentsUtils.getResponseMessageJson(String.format(Message.UPLOAD_CSV_FILE.getName(),
                                                          multipartFile.getOriginalFilename()));

        Mockito.doNothing().when(merchantService).save(multipartFile);

        mvc.perform(MockMvcRequestBuilders.multipart("/merchants/files")
                        .file(multipartFile)
                        .contentType("text/csv;charset=UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(jsonMer))
                .andReturn();
    }

    @Test
    public void uploadFileTest_exception() throws Exception {

        byte[] fileBytes = Files.readAllBytes(Paths.get("src/test/resources/merchants.csv"));
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                "merchants.csv",
                "text/csv",
                fileBytes);
        String jsonMer = PaymentsUtils.getResponseMessageJson(String.format(Message.COULD_NOT_UPLOAD_FILE.getName(),
                multipartFile.getOriginalFilename()));

        willThrow(new IOException()).given(merchantService).save(multipartFile);

        mvc.perform(MockMvcRequestBuilders.multipart("/merchants/files")
                        .file(multipartFile)
                        .contentType("text/csv;charset=UTF-8"))
                .andDo(print())
                .andExpect(status().isExpectationFailed())
                .andExpect(content().string(jsonMer))
                .andReturn();
    }
}
