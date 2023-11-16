package com.emerchantpay.paymentsystemtask.service.handler;

import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.dto.TransactionConverter;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.service.TransactionService;
import com.emerchantpay.paymentsystemtask.utils.TransactionUtils;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ChargeHandler extends TransactionHandler {

    Log log = LogFactory.getLog(this.getClass());
    @Autowired
    TransactionService service;

    @Override
    public List<TransactionDto> handleTransaction(TransactionDto transaction) throws TransactionException {
        List<TransactionDto> transactions = new ArrayList<>();

        if (transaction.getTransactionType().equals(TransactionType.CHARGE.getName())) {

            if(transaction.getStatus().equals(TransactionStatus.REFUNDED.name())){

                TransactionDto refundTransaction = createRefundTransaction(transaction);
                if (this.nextTransition != null) {
                    transactions.addAll(nextTransition.handleTransaction(refundTransaction));
                }
                return transactions;
            } else {
                TransactionDto authTransaction = null;
                if(!transaction.getStatus().equals(TransactionStatus.ERROR.getName())){
                    authTransaction =
                            service.findNonReferencedAuthTransByRefId(transaction.getReferenceIdentifier(), TransactionStatus.APPROVED);

                    if(authTransaction==null){
                        transaction.setStatus(TransactionStatus.ERROR.getName());
                        transaction.setReferenceIdentifier(null);
                        transaction.setMerchant(null);
                        log.info("There isn't Authorize transaction with the same reference Id");
                    }
                }
                if(transaction.getStatus().equals(TransactionStatus.APPROVED.name())) {
                    updateMerchantTotalAmount(transaction, authTransaction);
                }

                TransactionDto savedChargeTransaction = service.saveTransaction(TransactionConverter.convertToTransaction(transaction));
                transactions.add(savedChargeTransaction);
                return transactions;

            }


        } else {
            if (this.nextTransition != null) {
                return nextTransition.handleTransaction(transaction);
            }
        }
        return transactions;
    }

    private TransactionDto createRefundTransaction(TransactionDto dto) {
        TransactionDto transaction = new TransactionDto();
        transaction.setUuid(TransactionUtils.generateRandomUuid());
        transaction.setTransactionType(TransactionType.REFUND.getName());
        transaction.setStatus(TransactionStatus.APPROVED.name());
        transaction.setMerchant(dto.getMerchant());
        transaction.setAmount(dto.getAmount());
        transaction.setReferenceIdentifier(dto.getReferenceIdentifier());
        transaction.setCustomerPhone(dto.getCustomerPhone());
        transaction.setCustomerEmail(dto.getCustomerEmail());
        return transaction;
    }

    private void updateMerchantTotalAmount(TransactionDto transaction, TransactionDto authTrans){

        MerchantDto merchant = authTrans.getMerchant();
        if(transaction.getAmount().doubleValue()==authTrans.getAmount().doubleValue()){

            Double amount = merchant.getTotalTransactionSum() + transaction.getAmount();
            merchant.setTotalTransactionSum(amount);
        }else {
            transaction.setStatus(TransactionStatus.ERROR.getName());
            transaction.setReferenceIdentifier(null);
            log.info("The Charge Transaction amount is different form Authorize Transaction amount");
        }
        transaction.setMerchant(merchant);

    }
}
