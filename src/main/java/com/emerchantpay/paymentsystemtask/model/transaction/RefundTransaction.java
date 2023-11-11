package com.emerchantpay.paymentsystemtask.model.transaction;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Refund")
public class RefundTransaction extends Transaction{
}
