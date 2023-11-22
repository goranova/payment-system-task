package com.emerchantpay.paymentsystemtask.service.handler;

import com.emerchantpay.paymentsystemtask.dto.TransactionConverter;
import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.response.ResponseTransactionMessage;
import com.emerchantpay.paymentsystemtask.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorizeHandler extends TransactionHandler {

    @Autowired
    TransactionService trService;

    @Override
    public ResponseTransactionMessage handleTransaction(TransactionDto transaction) throws TransactionException {
        List<TransactionDto> transactions = new ArrayList<>();

        if (transaction.getTransactionType().equals(TransactionType.AUTHORIZE.getName())) {

            TransactionDto savedAuthTransaction =
                    trService.saveTransaction(TransactionConverter.convertToTransaction(transaction));
            transactions.add(savedAuthTransaction);

            this.message = Message.TRANSACTION_SAVED_SUCCESS.getName();
            return new ResponseTransactionMessage(message, transactions);
        } else {
            if (this.nextTransition != null) {
                return nextTransition.handleTransaction(transaction);
            }
        }
        return new ResponseTransactionMessage(Message.NO_TRANSACTIONS.getName(), transactions);
    }
}
