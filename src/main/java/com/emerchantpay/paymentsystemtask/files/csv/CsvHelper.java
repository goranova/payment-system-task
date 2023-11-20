package com.emerchantpay.paymentsystemtask.files.csv;

import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvHelper {

    public static final String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<MerchantDto> csvToMerchant(MultipartFile file) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<MerchantDto> merchants = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                MerchantDto merchant = new MerchantDto(
                        csvRecord.get("Description"),
                        csvRecord.get("Email"),
                        csvRecord.get("Status"),
                        Double.valueOf(csvRecord.get("Total Transaction Sum")));

                merchants.add(merchant);
            }

            return merchants;

        }
    }
}
