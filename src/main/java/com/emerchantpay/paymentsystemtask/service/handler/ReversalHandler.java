package com.emerchantpay.paymentsystemtask.service.handler;

import com.emerchantpay.paymentsystemtask.dto.TransactionConverter;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.service.TransactionService;
import com.emerchantpay.paymentsystemtask.validation.ReversalValidator;
import com.emerchantpay.paymentsystemtask.validation.TransactionValidator;
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

            TransactionValidator reversalValidator = new ReversalValidator();
            TransactionDto validatedTrans = reversalValidator.validate(reversalTrans);

            if(validatedTrans.getStatus().equals(TransactionStatus.APPROVED.name())){
                updateReferencedAuthTransaction(validatedTrans);
            }
            TransactionDto savedReversalTransaction = service.saveTransaction(TransactionConverter.convertToReversal(validatedTrans));
            transactions.add(savedReversalTransaction);

            return transactions;

        } else {
            if (this.nextTransition != null) {
                return nextTransition.handleTransaction(reversalTrans);
            }
        }
        return transactions;
    }

    public void updateReferencedAuthTransaction(TransactionDto reversalTransaction) {

        TransactionDto authTransaction =
                service.findAuthorizeTransactionByRefId(reversalTransaction.getReferenceIdentifier(), TransactionStatus.APPROVED);
        if(authTransaction!=null && authTransaction.getMerchant()!=null){
            long authMerchantId = authTransaction.getMerchant().getIdentifier().longValue();
            long reversalMerchantId = reversalTransaction.getMerchant().getIdentifier().longValue();

            if ( authMerchantId==reversalMerchantId ) {
                authTransaction.setStatus(TransactionStatus.REVERSED.name());
                service.saveTransaction(TransactionConverter.convertToAuthorize(authTransaction));
            }
        }
    }
}
