package com.emerchantpay.paymentsystemtask.dto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MerchantDto {

    private Long identifier;
    private String description;
    private String email;
    private String merchantStatus;
    private Double totalTransactionSum;
    private Set<TransactionDto> transactions = new HashSet<>();

    public MerchantDto (){}

    public MerchantDto(String description, String email, String merchantStatus, Double totalTransactionSum) {
        this.description = description;
        this.email = email;
        this.merchantStatus = merchantStatus;
        this.totalTransactionSum = totalTransactionSum;
    }

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

    public Set<TransactionDto> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<TransactionDto> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MerchantDto that)) return false;
        return  Objects.equals(getDescription(), that.getDescription())
                && Objects.equals(getEmail(), that.getEmail())
                && Objects.equals(getMerchantStatus(), that.getMerchantStatus())
                && Objects.equals(getTotalTransactionSum(), that.getTotalTransactionSum());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getEmail(), getMerchantStatus(), getTotalTransactionSum());
    }
}
