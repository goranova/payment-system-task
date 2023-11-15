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
                    refundTrans.setMerchant(null);
                }
            }

            if(refundTrans.getStatus().equals(TransactionStatus.APPROVED.name())){
                updateReferencedChargeTransaction(chargeTransaction, refundTrans);
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

        MerchantDto merchant=chargeTransaction.getMerchant();
        if (merchant.getTotalTransactionSum()>=refundTransaction.getAmount()){

            updateMerchantTotalAmount(refundTransaction, merchant);
            chargeTransaction.setStatus(TransactionStatus.REFUNDED.name());
            service.saveTransaction(TransactionConverter.convertToTransaction(chargeTransaction));
        } else{
            refundTransaction.setMerchant(merchant);
            refundTransaction.setStatus(TransactionStatus.ERROR.getName());
            refundTransaction.setReferenceIdentifier(null);
            log.info("The refund amount is bigger than Merchant Total transaction sum.");
        }
    }

    public void updateMerchantTotalAmount(TransactionDto transaction, MerchantDto merchant){

           Double amount = merchant.getTotalTransactionSum() - transaction.getAmount();
           merchant.setTotalTransactionSum(amount);
           transaction.setMerchant(merchant);
    }
}
