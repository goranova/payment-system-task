package com.emerchantpay.paymentsystemtask.service;

import com.emerchantpay.paymentsystemtask.dao.MerchantRepository;
import com.emerchantpay.paymentsystemtask.dto.MerchantConverter;
import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.model.Merchant;
import com.emerchantpay.paymentsystemtask.service.handler.TransactionHandlerService;
import com.emerchantpay.paymentsystemtask.validation.MerchantValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MerchantService {
    @Autowired
    MerchantRepository merchantRep;
    @Autowired
    TransactionHandlerService trHandlerService;


    public List<MerchantDto> findAllMerchants() {

        List<Merchant> merchants = merchantRep.findAll();
        return merchants.stream()
                .map(merchant -> MerchantConverter.convertToMerchantDto(merchant))
                .toList();


    }

    public MerchantDto findById(String id) {

        Long identifier = Long.valueOf(id);
        Optional<Merchant> merchant = merchantRep.findById(identifier);
        if (!merchant.isEmpty()) {
            return MerchantConverter.convertToMerchantDto(merchant.get());
        }
        return new MerchantDto();
    }

    public boolean editMerchant(MerchantDto existingMerchant, MerchantDto editedMerchant) {
        if (existingMerchant != null && editedMerchant != null) {

            MerchantValidator validator = new MerchantValidator();
            MerchantDto editedValidMerchant = validator.validate(editedMerchant);

            if (!editedMerchant.equals(editedValidMerchant)) {
                existingMerchant.setEmail(editedValidMerchant.getEmail());
                existingMerchant.setDescription(editedValidMerchant.getEmail());
                existingMerchant.setMerchantStatus(editedValidMerchant.getMerchantStatus());
                merchantRep.save(MerchantConverter.convertToMerchant(existingMerchant));
                return true;
            }
        }
        return false;
    }

    public List<MerchantDto> saveMerchants(List<MerchantDto> merchantsDto) {
        List<MerchantDto> merchantDtos = new ArrayList<>();
        for (MerchantDto mr : merchantsDto) {

            MerchantValidator validator = new MerchantValidator();
            MerchantDto validMerchant = validator.validate(mr);

            Merchant savedMerchant = merchantRep.save(MerchantConverter.convertToMerchant(validMerchant));
            MerchantDto savedMerchantDto = MerchantConverter.convertToMerchantDto(savedMerchant);
            merchantDtos.add(MerchantConverter.convertToMerchantDto(savedMerchant));

            if (!mr.getTransactions().isEmpty()) {
                mr.getTransactions().stream()
                        .forEach(tr -> tr.setMerchant(savedMerchantDto));
                trHandlerService.handleTransactionChain(mr.getTransactions());
            }
        }

        return merchantDtos;
    }

    public void deleteMerchant(MerchantDto merchantDto){
        merchantRep.delete(MerchantConverter.convertToMerchant(merchantDto));
    }
}