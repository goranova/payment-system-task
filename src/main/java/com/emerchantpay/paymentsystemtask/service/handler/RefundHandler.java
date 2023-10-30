package com.emerchantpay.paymentsystemtask.service.handler;

import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.dto.TransactionConverter;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.MerchantStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.service.TransactionService;
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

            if(refundTrans.getStatus().equals(TransactionStatus.APPROVED.name())){

                TransactionDto chargeTransaction =
                        service.findChargeTransactionByRefId(refundTrans.getReferenceIdentifier(), TransactionStatus.APPROVED);

                if (chargeTransaction != null
                        && chargeTransaction.getMerchant()!=null
                        && chargeTransaction.getMerchant().getMerchantStatus().equals(MerchantStatus.ACTIVE.name())) {

                    updateReferencedChargeTransaction(chargeTransaction, refundTrans);
                    TransactionDto savedRefundTransaction =
                            service.saveTransaction(TransactionConverter.convertToRefund(refundTrans));
                    transactions.add(savedRefundTransaction);
                }
            }

            return transactions;

        } else {
            if (this.nextTransition != null) {
                return nextTransition.handleTransaction(refundTrans);
            }
        }
        return transactions;
    }

    public void updateReferencedChargeTransaction(TransactionDto chargeTransaction, TransactionDto refundTransaction) {

        Long chargeMerchantId = chargeTransaction.getMerchant().getIdentifier();

        if (chargeTransaction.getAmount().doubleValue() >= refundTransaction.getAmount().doubleValue()
                && chargeMerchantId!=null) {

            updateMerchantTotalAmount(refundTransaction, chargeTransaction.getMerchant());
            chargeTransaction.setStatus(TransactionStatus.REFUNDED.name());
            service.saveTransaction(TransactionConverter.convertToCharge(chargeTransaction));

        }
    }

    public void updateMerchantTotalAmount(TransactionDto transaction, MerchantDto merchant){

        Double amount = merchant.getTotalTransactionSum() - transaction.getAmount();
        merchant.setTotalTransactionSum(amount);
        transaction.setMerchant(merchant);

    }
}
