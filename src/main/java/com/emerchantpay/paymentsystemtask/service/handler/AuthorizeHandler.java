package com.emerchantpay.paymentsystemtask.service.handler;

import com.emerchantpay.paymentsystemtask.dto.TransactionConverter;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.service.TransactionService;
import com.emerchantpay.paymentsystemtask.utils.TransactionUtils;
import com.emerchantpay.paymentsystemtask.validation.AuthorizeValidator;
import com.emerchantpay.paymentsystemtask.validation.TransactionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class AuthorizeHandler extends TransactionHandler {

    @Autowired
   TransactionService service;
    @Override
    public List<TransactionDto> handleTransaction(TransactionDto dto) {
      List<TransactionDto>  transactions = new ArrayList<>();

        if (dto.getTransactionType().equals(TransactionType.AUTHORIZE.getName())) {

            TransactionValidator authValidator = new AuthorizeValidator();
            TransactionDto validatedTrans = authValidator.validate(dto);
            TransactionDto savedAuthTransaction = service.saveTransaction(TransactionConverter.convertToAuthorize(validatedTrans));
            transactions.add(savedAuthTransaction);
            if (validatedTrans.getStatus().equals(TransactionStatus.APPROVED.name())) {
                TransactionDto chargeTransaction = createChargedTransaction(validatedTrans);
                if (this.nextTransition != null) {
                    transactions.addAll(nextTransition.handleTransaction(chargeTransaction));
                }
                return transactions;

            }
        } else {
            if (this.nextTransition != null) {
                return nextTransition.handleTransaction(dto);
            }
        }
        return transactions;
    }

    public TransactionDto createChargedTransaction(TransactionDto dto) {
        TransactionDto transaction = new TransactionDto();
        transaction.setUuid(TransactionUtils.generateRandomUuid());
        transaction.setTransactionType(TransactionType.CHARGE.getName());
        transaction.setStatus(TransactionStatus.APPROVED.name());
        transaction.setMerchant(dto.getMerchant());
        transaction.setAmount(dto.getAmount());
        transaction.setReferenceIdentifier(dto.getUuid());
        transaction.setCustomerPhone(dto.getCustomerPhone());
        transaction.setCustomerEmail(dto.getCustomerEmail());

        return transaction;
    }

}
