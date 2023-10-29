package com.emerchantpay.paymentsystemtask.model.transaction;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Reversal")
public class ReversalTransaction extends Transaction {
}
