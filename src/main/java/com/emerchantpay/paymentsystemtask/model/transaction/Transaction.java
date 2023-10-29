package com.emerchantpay.paymentsystemtask.model.transaction;

import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.model.Merchant;
import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
public class Transaction implements Serializable {
    @Id
    @Column(name = "UUID")
    private String uuid;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private TransactionStatus status;

    @Column(name="TYPE", insertable = false, updatable = false)
    private String transactionType;
    @Column(name = "CUSTOMER_EMAIL")
    private String customerEmail;
    @Column(name = "CUSTOMER_PHONE")
    private String customerPhone;
    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "MERCHANT_ID")
    private Merchant merchant;

    @Column(name = "REFERENCE_ID")
    private String referenceIdentifier;

    @Column(name = "TIMESTMP")
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    public Transaction() {
    }


    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid=uuid;
    }
    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String getReferenceIdentifier() {
        return referenceIdentifier;
    }

    public void setReferenceIdentifier(String referenceIdentifier) {
        this.referenceIdentifier = referenceIdentifier;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }


}
