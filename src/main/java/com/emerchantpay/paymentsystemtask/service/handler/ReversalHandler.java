package com.emerchantpay.paymentsystemtask.service.handler;

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
public class ReversalHandler extends TransactionHandler {

    Log log = LogFactory.getLog(this.getClass());
    @Autowired
    TransactionService service;
    @Override
    protected ResponseTransactionMessage handleTransaction(TransactionDto reversalTrans) throws TransactionException {

        List<TransactionDto> transactions = new ArrayList<>();

        if (reversalTrans.getTransactionType().equals(TransactionType.REVERSAL.getName())) {

            TransactionDto authTransaction;

            if(reversalTrans.getStatus().equals(TransactionStatus.APPROVED.name())){
                authTransaction =
                        service.findNonReferencedAuthTransByRefIdMerId(reversalTrans.getReferenceIdentifier(),
                                                                 TransactionStatus.APPROVED,
                                                                 String.valueOf(reversalTrans.getMerchant().getIdentifier()));

                if(authTransaction==null){
                    this.message =
                            String.format(Message.TRANSACTION_REFERENCE.getName(),
                                    TransactionType.AUTHORIZE.getName(), reversalTrans.getReferenceIdentifier());
                    log.info(this.message);

                    reversalTrans.setStatus(TransactionStatus.ERROR.getName());
                    reversalTrans.setReferenceIdentifier(null);
                }else {
                    updateReferencedAuthTransaction(authTransaction);
                    transactions.add(authTransaction);
                }
            }
            TransactionDto savedReversalTransaction =
                    service.saveTransaction(TransactionConverter.convertToTransaction(reversalTrans));
            transactions.add(savedReversalTransaction);


            return new ResponseTransactionMessage(message, transactions);

        } else {
            if (this.nextTransition != null) {
                return nextTransition.handleTransaction(reversalTrans);
            }
        }
        return new ResponseTransactionMessage(Message.NO_TRANSACTIONS.getName(), transactions);
    }

    public void updateReferencedAuthTransaction(TransactionDto authTransaction) {

        authTransaction.setStatus(TransactionStatus.REVERSED.name());
        service.saveTransaction(TransactionConverter.convertToTransaction(authTransaction));
        this.message = Message.TRANSACTION_SAVED_SUCCESS.getName();
    }

}
