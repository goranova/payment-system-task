package com.emerchantpay.paymentsystemtask.model.transaction;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Authorize")
public class AuthorizeTransaction extends Transaction {
}
