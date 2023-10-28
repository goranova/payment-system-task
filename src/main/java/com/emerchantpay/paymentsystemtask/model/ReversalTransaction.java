package com.emerchantpay.paymentsystemtask.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Reversal")
public class ReversalTransaction extends Transaction {
}
