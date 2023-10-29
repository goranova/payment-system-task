package com.emerchantpay.paymentsystemtask.model;

import com.emerchantpay.paymentsystemtask.enums.MerchantStatus;
import com.emerchantpay.paymentsystemtask.model.transaction.Transaction;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Merchant implements Serializable {

    @SequenceGenerator(name = "S_MERCHANT", sequenceName = "S_MERCHANT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_MERCHANT")
    @Id
    @Column(name = "ID")
    private Long identifier;
    @Column(name="DESCRIPTION")
    private String description;
    @Column(name="EMAIL")
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(name="STATUS")
    private MerchantStatus status;
    @Column(name="TOTAL_TRANSACTION_SUM")
    private Double totalTransactionSum;

    @OneToMany(mappedBy="merchant",
            fetch=FetchType.LAZY
    )
    private Set<Transaction> transactions = new HashSet<>();

    public Merchant (){}

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

    public MerchantStatus getStatus() {
        return status;
    }

    public void setStatus(MerchantStatus status) {
        this.status = status;
    }

    public Double getTotalTransactionSum() {
        return totalTransactionSum;
    }

    public void setTotalTransactionSum(Double totalTransactionSum) {
        this.totalTransactionSum = totalTransactionSum;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setMerchant(this);
    }

}
