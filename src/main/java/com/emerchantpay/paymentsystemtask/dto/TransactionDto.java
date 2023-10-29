package com.emerchantpay.paymentsystemtask.dto;

import com.emerchantpay.paymentsystemtask.model.Merchant;

public class TransactionDto {
    private String uuid;
    private String status;
    private String transactionType;
    private String customerEmail;
    private String customerPhone;
    private Double amount;
    private String referenceIdentifier;

    private MerchantDto merchant;

    private String timestamp ;

    public String getUuid() {
        return uuid;
    }
    public String getStatus() {
        return status;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }


    public Double getAmount() {
        return amount;
    }

    public String getReferenceIdentifier() {
        return referenceIdentifier;
    }

    public MerchantDto getMerchant(){
        return merchant;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setReferenceIdentifier(String referenceIdentifier) {
        this.referenceIdentifier = referenceIdentifier;
    }

    public void setMerchant(MerchantDto merchant){
        this.merchant = merchant;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
