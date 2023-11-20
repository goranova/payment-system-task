package com.emerchantpay.paymentsystemtask.service;

import com.emerchantpay.paymentsystemtask.dao.MerchantRepository;
import com.emerchantpay.paymentsystemtask.dto.MerchantConverter;
import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.enums.MerchantStatus;
import com.emerchantpay.paymentsystemtask.exceptions.MerchantException;
import com.emerchantpay.paymentsystemtask.model.Merchant;
import com.emerchantpay.paymentsystemtask.files.csv.CsvHelper;
import com.emerchantpay.paymentsystemtask.validation.MerchantValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MerchantService {

    @Autowired
    MerchantRepository merchantRep;


    public List<MerchantDto> findAllMerchants() {

        List<Merchant> merchants = merchantRep.findAll();
        return merchants.stream()
                .map(MerchantConverter::convertToMerchantDto)
                .toList();
    }

    public MerchantDto findById(String id) {

        Long identifier = Long.valueOf(id);
        Optional<Merchant> merchant = merchantRep.findById(identifier);
        return merchant.map(mr->MerchantConverter.convertToMerchantWithTransactionsDto(mr, mr.getTransactions()))
                .orElse(new MerchantDto());
    }

    public MerchantDto findMerchantByDescrStatus(String descr, String status) {

       Merchant mer =
               merchantRep.findMerchantByDescrStatus(descr, MerchantStatus.valueOf(status));
       if(mer!=null) {
           return MerchantConverter.convertToMerchantWithTransactionsDto(mer,mer.getTransactions());
       }
       return null;
    }

    public MerchantDto processMerchant(MerchantDto merchant) throws MerchantException {

        MerchantValidator validator = new MerchantValidator();
        MerchantDto validMerchant = validator.validate(merchant);

        MerchantDto existingMerchant =
                findMerchantByDescrStatus(validMerchant.getDescription(), validMerchant.getMerchantStatus());

        if (existingMerchant != null) {
            if (!existingMerchant.equals(validMerchant)) {
                validMerchant.setIdentifier(existingMerchant.getIdentifier());
            } else {
                return existingMerchant;
            }
        }
        return save(MerchantConverter.convertToMerchant(validMerchant));
    }

    public boolean editMerchant(MerchantDto existingMerchant, MerchantDto editedMerchant) throws MerchantException {

        if (existingMerchant != null && editedMerchant != null) {

            MerchantValidator validator = new MerchantValidator();
            MerchantDto editedValidMerchant = validator.validate(editedMerchant);

            if (!editedValidMerchant.equals(existingMerchant)) {
                editedMerchant.setIdentifier(existingMerchant.getIdentifier());
                merchantRep.save(MerchantConverter.convertToMerchant(editedMerchant));
                return true;
            }
        }
        return false;
    }

    public void save(MultipartFile file) throws IOException, MerchantException {

        List<MerchantDto> merchants = CsvHelper.csvToMerchant(file);
        for(MerchantDto mer: merchants) {
            processMerchant(mer);
        }
    }

    public MerchantDto save(Merchant merchant) {
        merchantRep.save(merchant);
        return MerchantConverter.convertToMerchantDto(merchant);
    }

    public void deleteMerchant(MerchantDto merchantDto) {
        merchantRep.delete(MerchantConverter.convertToMerchant(merchantDto));
    }
}