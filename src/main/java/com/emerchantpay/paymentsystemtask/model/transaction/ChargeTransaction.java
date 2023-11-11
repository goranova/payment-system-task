package com.emerchantpay.paymentsystemtask.model.transaction;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Charge")
public class ChargeTransaction extends Transaction {
}
