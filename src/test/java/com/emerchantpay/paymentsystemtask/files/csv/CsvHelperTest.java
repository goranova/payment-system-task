package com.emerchantpay.paymentsystemtask.files.csv;

import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvHelperTest {

    @Test
    public void csvToMerchantTest_success () throws IOException {

      byte[] fileBytes = Files.readAllBytes(Paths.get("src/test/resources/merchants.csv"));

      MockMultipartFile multipartFile = new MockMultipartFile("file",
                                         "merchants.csv",
                                         "text/csv",
                                          fileBytes);
      List<MerchantDto> merchants=CsvHelper.csvToMerchant(multipartFile);
      assertEquals(6, merchants.size());
    }

    @Test
    public void csvToMerchantTest_fail() throws IOException {

        byte[] fileBytes = Files.readAllBytes(Paths.get("src/test/resources/merchants.txt"));

        MockMultipartFile multipartFile = new MockMultipartFile("file",
                "merchants.txt",
                MediaType.TEXT_PLAIN_VALUE,
                fileBytes);

        List<MerchantDto> merchants=CsvHelper.csvToMerchant(multipartFile);
        assertTrue(merchants.isEmpty());
    }
}
