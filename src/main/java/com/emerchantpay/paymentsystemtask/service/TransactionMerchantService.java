package com.emerchantpay.paymentsystemtask.service;

import com.emerchantpay.paymentsystemtask.dto.MerchantConverter;
import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.error.MissingMerchantException;
import com.emerchantpay.paymentsystemtask.service.handler.TransactionHandlerService;
import com.emerchantpay.paymentsystemtask.validation.MerchantValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionMerchantService {

    private static final String MISSING_MERCHANT_MESSAGE = "The merchant is missing";
    @Autowired
    MerchantService merchantService;

    @Autowired
    TransactionHandlerService transHandler;


    public List<MerchantDto> saveMerchants(List<MerchantDto> merchantsDto) {
        List<MerchantDto> merchantDtos = new ArrayList<>();
        for (MerchantDto mr : merchantsDto) {

            MerchantValidator validator = new MerchantValidator();
            MerchantDto validMerchant = validator.validate(mr);

            MerchantDto savedMerchant = merchantService.save(MerchantConverter.convertToMerchant(validMerchant));
            merchantDtos.add(savedMerchant);

            if (!mr.getTransactions().isEmpty()) {
                mr.getTransactions().stream()
                        .forEach(tr -> tr.setMerchant(savedMerchant));
                transHandler.handleTransactionChain(mr.getTransactions());
            }
        }

        return merchantDtos;
    }

    public List<TransactionDto> handleTransactionChain(List<TransactionDto> transactions) throws MissingMerchantException {

        for(TransactionDto trans: transactions){
            MerchantDto merchantDto = trans.getMerchant();
            if(merchantDto == null){
                throw new MissingMerchantException(MISSING_MERCHANT_MESSAGE);
            }
            if(merchantDto.getIdentifier() != null){
                MerchantDto existingMer =
                        merchantService.findById(String.valueOf(merchantDto.getIdentifier()));
                if ( existingMer!=null ){
                    trans.setMerchant(existingMer);
                }
            }
            MerchantValidator merValidator = new MerchantValidator();
            merValidator.validate(merchantDto);
        }
        return transHandler.handleTransactionChain(transactions);
    }

    public List<TransactionDto> findTransactions() {
        return transHandler.findTransactions();
    }
}
