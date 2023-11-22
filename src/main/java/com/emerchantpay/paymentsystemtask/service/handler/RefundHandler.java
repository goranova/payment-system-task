package com.emerchantpay.paymentsystemtask.service.handler;

import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.dto.TransactionConverter;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.response.ResponseTransactionMessage;
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
    public ResponseTransactionMessage handleTransaction(TransactionDto refundTrans) throws TransactionException {

        List<TransactionDto> transactions = new ArrayList<>();

        if (refundTrans.getTransactionType().equals(TransactionType.REFUND.getName())) {

            TransactionDto chargeTransaction;
            if(refundTrans.getStatus().equals(TransactionStatus.APPROVED.name())){

                 chargeTransaction =
                         service.findNonRefundedChargeTransByRefIdMerId(refundTrans.getReferenceIdentifier(),
                                 TransactionStatus.APPROVED,
                                 String.valueOf(refundTrans.getMerchant().getIdentifier())
                                 );

                if(chargeTransaction==null){
                    this.message =
                            String.format(Message.TRANSACTION_REFERENCE.getName(),
                                    TransactionType.CHARGE.getName(),refundTrans.getReferenceIdentifier());
                    log.info(this.message);

                    refundTrans.setStatus(TransactionStatus.ERROR.getName());
                    refundTrans.setReferenceIdentifier(null);
                }else {
                    updateReferencedChargeTransaction(chargeTransaction, refundTrans);
                    transactions.add(chargeTransaction);
                }

            }
            TransactionDto savedRefundTransaction =
                    service.saveTransaction(TransactionConverter.convertToTransaction(refundTrans));
            transactions.add(savedRefundTransaction);

            return new ResponseTransactionMessage(message, transactions);

        } else {
            if (this.nextTransition != null) {
                return nextTransition.handleTransaction(refundTrans);
            }
        }
        return new ResponseTransactionMessage(Message.NO_TRANSACTIONS.getName(), transactions);
    }

    public void updateReferencedChargeTransaction(TransactionDto chargeTransaction, TransactionDto refundTransaction) {

        MerchantDto merchant=refundTransaction.getMerchant();
        if (merchant.getTotalTransactionSum().doubleValue()>=refundTransaction.getAmount().doubleValue()){

            updateMerchantTotalAmount(refundTransaction, merchant);
            chargeTransaction.setStatus(TransactionStatus.REFUNDED.name());
            service.saveTransaction(TransactionConverter.convertToTransaction(chargeTransaction));
            this.message = Message.TRANSACTION_SAVED_SUCCESS.getName();

        } else{
            refundTransaction.setStatus(TransactionStatus.ERROR.getName());
            refundTransaction.setReferenceIdentifier(null);
            this.message = String.format(Message.TRANSACTION_DIFF_AMOUNT.getName(),
                    refundTransaction.getTransactionType(),"Merchant Total Transaction");
            log.info(this.message);
        }
    }

    public void updateMerchantTotalAmount(TransactionDto transaction, MerchantDto merchant){

           Double amount = merchant.getTotalTransactionSum() - transaction.getAmount();
           merchant.setTotalTransactionSum(amount);
    }
}
