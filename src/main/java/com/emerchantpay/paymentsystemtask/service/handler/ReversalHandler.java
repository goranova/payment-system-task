package com.emerchantpay.paymentsystemtask.service.handler;

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
public class ReversalHandler extends TransactionHandler {
    @Autowired
    TransactionService service;
    @Override
    protected List<TransactionDto> handleTransaction(TransactionDto reversalTrans) {

        List<TransactionDto> transactions = new ArrayList<>();

        if (reversalTrans.getTransactionType().equals(TransactionType.REVERSAL.getName())) {

            if(reversalTrans.getStatus().equals(TransactionStatus.APPROVED.name())){

                TransactionDto authTransaction =
                        service.findAuthorizeTransactionByRefId(reversalTrans.getReferenceIdentifier(), TransactionStatus.APPROVED);
                if(authTransaction!=null
                        && authTransaction.getMerchant()!=null
                        && authTransaction.getMerchant().getMerchantStatus().equals(MerchantStatus.ACTIVE.name() )) {

                    updateReferencedAuthTransaction(authTransaction);
                    TransactionDto savedReversalTransaction =
                            service.saveTransaction(TransactionConverter.convertToTransaction(reversalTrans));
                    transactions.add(savedReversalTransaction);
                }

            }


            return transactions;

        } else {
            if (this.nextTransition != null) {
                return nextTransition.handleTransaction(reversalTrans);
            }
        }
        return transactions;
    }

    public void updateReferencedAuthTransaction(TransactionDto authTransaction) {

        authTransaction.setStatus(TransactionStatus.REVERSED.name());
        service.saveTransaction(TransactionConverter.convertToTransaction(authTransaction));
    }

}
