package com.emerchantpay.paymentsystemtask.dto;

import java.util.*;

public class MerchantDto {

    private Long identifier;
    private String description;
    private String email;

    private String merchantStatus;
    private Double totalTransactionSum;
    private List<TransactionDto> transactions = new ArrayList<>();

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMerchantStatus() {
        return merchantStatus;
    }

    public void setMerchantStatus(String merchantStatus) {
        this.merchantStatus = merchantStatus;
    }

    public Double getTotalTransactionSum() {
        return totalTransactionSum;
    }

    public void setTotalTransactionSum(Double totalTransactionSum) {
        this.totalTransactionSum = totalTransactionSum;
    }

    public List<TransactionDto> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDto> transactions) {
        this.transactions = transactions;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MerchantDto that)) return false;
        return  Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getMerchantStatus(), that.getMerchantStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdentifier(), getDescription(), getEmail(), getMerchantStatus());
    }
}
