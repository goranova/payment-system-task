package com.emerchantpay.paymentsystemtask.service.handler;

import com.emerchantpay.paymentsystemtask.dto.TransactionConverter;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.service.TransactionService;
import com.emerchantpay.paymentsystemtask.utils.TransactionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorizeHandler extends TransactionHandler {

    @Autowired
    TransactionService trService;

    @Override
    public List<TransactionDto> handleTransaction(TransactionDto transaction) throws TransactionException {
        List<TransactionDto> transactions = new ArrayList<>();

        if (transaction.getTransactionType().equals(TransactionType.AUTHORIZE.getName())) {

            TransactionDto savedAuthTransaction =
                    trService.saveTransaction(TransactionConverter.convertToTransaction(transaction));
            transactions.add(savedAuthTransaction);

            if (savedAuthTransaction.getStatus().equals(TransactionStatus.APPROVED.name())) {
                TransactionDto chargeTransaction = createChargedTransaction(savedAuthTransaction);
                if (this.nextTransition != null) {
                    transactions.addAll(nextTransition.handleTransaction(chargeTransaction));
                }
                return transactions;

            }
        } else {
            if (this.nextTransition != null) {
                return nextTransition.handleTransaction(transaction);
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
