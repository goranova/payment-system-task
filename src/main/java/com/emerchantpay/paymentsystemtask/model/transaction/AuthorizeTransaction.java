package com.emerchantpay.paymentsystemtask.model.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Authorize")
public class AuthorizeTransaction extends Transaction {

    @Column(name = "AMOUNT")
    private Double amount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
