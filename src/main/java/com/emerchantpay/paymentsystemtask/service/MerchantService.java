package com.emerchantpay.paymentsystemtask.service;

import com.emerchantpay.paymentsystemtask.dao.MerchantRepository;
import com.emerchantpay.paymentsystemtask.dto.MerchantConverter;
import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.exceptions.MerchantException;
import com.emerchantpay.paymentsystemtask.model.Merchant;
import com.emerchantpay.paymentsystemtask.validation.MerchantValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return merchant.map(MerchantConverter::convertToMerchantDto).orElse(null);
    }

    public MerchantDto findExistingMerchant(MerchantDto merchant) throws MerchantException {

        if(merchant == null){
            throw new MerchantException(Message.MISSING_MERCHANT.getName());
        }
        if(merchant.getIdentifier() != null){
            MerchantDto existingMer = findById(String.valueOf(merchant.getIdentifier()));
            if ( existingMer!=null ){
               return existingMer;
            }
        }
        return merchant;
    }




    public boolean editMerchant(MerchantDto existingMerchant, MerchantDto editedMerchant) throws MerchantException {
        if (existingMerchant != null && editedMerchant != null) {

            MerchantValidator validator = new MerchantValidator();
            MerchantDto editedValidMerchant = validator.validate(editedMerchant);

            if (!editedValidMerchant.equals(existingMerchant)) {
                existingMerchant.setEmail(editedValidMerchant.getEmail());
                existingMerchant.setDescription(editedValidMerchant.getDescription());
                existingMerchant.setMerchantStatus(editedValidMerchant.getMerchantStatus());
                merchantRep.save(MerchantConverter.convertToMerchant(existingMerchant));
                return true;
            }
        }
        return false;
    }

    public MerchantDto save(Merchant merchant) {
        merchantRep.save(merchant);
        return MerchantConverter.convertToMerchantDto(merchant);
    }

    public void deleteMerchant(MerchantDto merchantDto) {
        merchantRep.delete(MerchantConverter.convertToMerchant(merchantDto));
    }
}