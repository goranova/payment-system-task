package com.emerchantpay.paymentsystemtask.service.handler;

import com.emerchantpay.paymentsystemtask.dto.TransactionConverter;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.model.Merchant;
import com.emerchantpay.paymentsystemtask.service.TransactionService;
import com.emerchantpay.paymentsystemtask.utils.TransactionUtils;
import com.emerchantpay.paymentsystemtask.validation.ChargeValidator;
import com.emerchantpay.paymentsystemtask.validation.TransactionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ChargeHandler extends TransactionHandler {
    @Autowired
    TransactionService service;
    @Override
    public List<TransactionDto> handleTransaction(TransactionDto transaction) {
        List<TransactionDto> transactions = new ArrayList<>();

        if (transaction.getTransactionType().equals(TransactionType.CHARGE.getName())) {

            TransactionValidator chargeValidator = new ChargeValidator();
            TransactionDto validatedTrans = chargeValidator.validate(transaction);

            updateMerchantTotalAmount(validatedTrans);

            TransactionDto savedChargeTransaction = service.saveTransaction(TransactionConverter.convertToCharge(validatedTrans));
            transactions.add(savedChargeTransaction);

            if(validatedTrans.getStatus().equals(TransactionStatus.REFUNDED.name())){

                TransactionDto refundTransaction = createRefundTransaction(validatedTrans);
                if (this.nextTransition != null) {
                    transactions.addAll(nextTransition.handleTransaction(refundTransaction));
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

    public TransactionDto createRefundTransaction(TransactionDto dto) {
        TransactionDto transaction = new TransactionDto();
        transaction.setUuid(TransactionUtils.generateRandomUuid());
        transaction.setTransactionType(TransactionType.REFUND.name());
        transaction.setStatus(TransactionStatus.APPROVED.name());
        transaction.setMerchant(dto.getMerchant());
        transaction.setAmount(dto.getAmount());
        transaction.setReferenceIdentifier(dto.getReferenceIdentifier());
        transaction.setCustomerPhone(dto.getCustomerPhone());
        transaction.setCustomerEmail(dto.getCustomerEmail());
        return transaction;
    }

    public void updateMerchantTotalAmount(TransactionDto transaction){
        if(transaction.getStatus().equals(TransactionStatus.APPROVED.name())) {
            //validateMerchant
            Merchant merchant = transaction.getMerchant();
            Double amount = merchant.getTotalTransactionSum() + transaction.getAmount();
            merchant.setTotalTransactionSum(amount);
            transaction.setMerchant(merchant);
        }
    }
}