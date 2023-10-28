package com.emerchantpay.paymentsystemtask.service.handler;

import com.emerchantpay.paymentsystemtask.dto.TransactionConverter;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.model.Merchant;
import com.emerchantpay.paymentsystemtask.service.TransactionService;
import com.emerchantpay.paymentsystemtask.validation.RefundValidator;
import com.emerchantpay.paymentsystemtask.validation.TransactionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RefundHandler extends TransactionHandler {
    @Autowired
    TransactionService service;
    @Override
    public List<TransactionDto> handleTransaction(TransactionDto refundTrans) {

        List<TransactionDto> transactions = new ArrayList<>();

        if (refundTrans.getTransactionType().equals(TransactionType.REFUND.getName())) {

            TransactionValidator refundValidator = new RefundValidator();
            TransactionDto validatedTrans = refundValidator.validate(refundTrans);

            if(validatedTrans.getStatus().equals(TransactionStatus.APPROVED.name())){
                updateReferencedChargeTransaction(validatedTrans);
            }
            TransactionDto savedRefundTransaction = service.saveTransaction(TransactionConverter.convertToRefund(validatedTrans));
            transactions.add(savedRefundTransaction);

            return transactions;

        } else {
            if (this.nextTransition != null) {
                return nextTransition.handleTransaction(refundTrans);
            }
        }
        return transactions;
    }

    public void updateReferencedChargeTransaction(TransactionDto refundTransaction) {

        TransactionDto chargeTransaction =
                service.findChargeTransactionByRefId(refundTransaction.getReferenceIdentifier(), TransactionStatus.APPROVED);

        if (chargeTransaction != null && chargeTransaction.getMerchant()!=null) {

            long chargeMerchantId = chargeTransaction.getMerchant().getIdentifier().longValue();
            long refundMerchantId = refundTransaction.getMerchant().getIdentifier().longValue();

            if (chargeTransaction.getAmount().doubleValue() >= refundTransaction.getAmount().doubleValue()
                    && chargeMerchantId==refundMerchantId) {
                updateMerchantTotalAmount(refundTransaction);
                chargeTransaction.setStatus(TransactionStatus.REFUNDED.name());

            }
            service.saveTransaction(TransactionConverter.convertToCharge(chargeTransaction));
        }
    }

    public void updateMerchantTotalAmount(TransactionDto transaction){

        //validateMerchant
        Merchant merchant = transaction.getMerchant();
        Double amount = merchant.getTotalTransactionSum() - transaction.getAmount();
        merchant.setTotalTransactionSum(amount);
        transaction.setMerchant(merchant);

    }
}
