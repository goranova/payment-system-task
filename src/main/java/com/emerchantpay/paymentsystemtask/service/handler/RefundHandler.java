package com.emerchantpay.paymentsystemtask.service.handler;

import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.dto.TransactionConverter;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.service.TransactionService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RefundHandler extends TransactionHandler {

    Log log = LogFactory.getLog(this.getClass());
    @Autowired
    TransactionService service;
    @Override
    public List<TransactionDto> handleTransaction(TransactionDto refundTrans) throws TransactionException {

        List<TransactionDto> transactions = new ArrayList<>();

        if (refundTrans.getTransactionType().equals(TransactionType.REFUND.getName())) {

            TransactionDto chargeTransaction=null;
            if(!refundTrans.getStatus().equals(TransactionStatus.ERROR.getName())){

                 chargeTransaction =
                         service.findNonRefundedChargeTransByRefId(refundTrans.getReferenceIdentifier(), TransactionStatus.APPROVED);

                if(chargeTransaction==null){
                    refundTrans.setStatus(TransactionStatus.ERROR.getName());
                    refundTrans.setReferenceIdentifier(null);
                }
            }

            if(refundTrans.getStatus().equals(TransactionStatus.APPROVED.name())){

                if ( chargeTransaction.getMerchant()!=null
                        && chargeTransaction.getMerchant().equals(refundTrans.getMerchant())) {

                    updateReferencedChargeTransaction(chargeTransaction, refundTrans);
                } else {
                    log.warn("The merchants for Charge and Refund transactions are different.");
                    refundTrans.setStatus(TransactionStatus.ERROR.getName());
                    refundTrans.setReferenceIdentifier(null);
                }
            }
            TransactionDto savedRefundTransaction =
                    service.saveTransaction(TransactionConverter.convertToTransaction(refundTrans));
            transactions.add(savedRefundTransaction);

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
            service.saveTransaction(TransactionConverter.convertToTransaction(chargeTransaction));

        }
    }

    public void updateMerchantTotalAmount(TransactionDto transaction, MerchantDto merchant){

        Double amount = merchant.getTotalTransactionSum() - transaction.getAmount();
        merchant.setTotalTransactionSum(amount);
        transaction.setMerchant(merchant);

    }
}
