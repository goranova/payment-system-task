package com.emerchantpay.paymentsystemtask.response;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;

import java.util.List;

public class ResponseTransactionMessage {
    private String message;

    private List<TransactionDto> transactions;

    public ResponseTransactionMessage(String message, List<TransactionDto> transactions) {
        this.message = message;
        this.transactions = transactions;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TransactionDto> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDto> transactions) {
        this.transactions = transactions;
    }
}
