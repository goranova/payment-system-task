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
public class ChargeHandler extends TransactionHandler {

    Log log = LogFactory.getLog(this.getClass());
    @Autowired
    TransactionService service;

    @Override
    public List<TransactionDto> handleTransaction(TransactionDto transaction) throws TransactionException {
        List<TransactionDto> transactions = new ArrayList<>();

        if (transaction.getTransactionType().equals(TransactionType.CHARGE.getName())) {

            TransactionDto authTransaction;
            if(transaction.getStatus().equals(TransactionStatus.APPROVED.getName())){
                authTransaction =
                        service.findNonReferencedAuthTransByRefIdMerId(transaction.getReferenceIdentifier(),
                                                                 TransactionStatus.APPROVED,
                                                                 String.valueOf(transaction.getMerchant().getIdentifier()));

                if(authTransaction==null){
                    transaction.setStatus(TransactionStatus.ERROR.getName());
                    transaction.setReferenceIdentifier(null);
                    log.info("There isn't Authorize transaction with the same reference Id");
                }else {
                    updateMerchantTotalAmount(transaction, authTransaction);
                }

            }

            TransactionDto savedChargeTransaction = service.saveTransaction(TransactionConverter.convertToTransaction(transaction));
            transactions.add(savedChargeTransaction);
            return transactions;

        } else {
            if (this.nextTransition != null) {
                return nextTransition.handleTransaction(transaction);
            }
        }
        return transactions;
    }

    private void updateMerchantTotalAmount(TransactionDto transaction, TransactionDto authTrans){

        MerchantDto merchant = transaction.getMerchant();
        if(transaction.getAmount().doubleValue()==authTrans.getAmount().doubleValue()){

            Double amount = merchant.getTotalTransactionSum() + transaction.getAmount();
            merchant.setTotalTransactionSum(amount);
        }else {
            transaction.setStatus(TransactionStatus.ERROR.getName());
            transaction.setReferenceIdentifier(null);
            log.info("The Charge Transaction amount is different form Authorize Transaction amount");
        }
    }
}
