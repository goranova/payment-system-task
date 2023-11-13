package com.emerchantpay.paymentsystemtask.dao;

import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.model.transaction.AuthorizeTransaction;
import com.emerchantpay.paymentsystemtask.model.transaction.ChargeTransaction;
import com.emerchantpay.paymentsystemtask.model.transaction.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query("select transaction from Transaction transaction")
    List<Transaction> findTransactions();

    @Query("select charge from ChargeTransaction charge " +
            "where charge.referenceIdentifier=:referenceIdentifier " +
            "and charge.status=:status")
    List<ChargeTransaction> findChargeTransactionByRefId(
            @Param("referenceIdentifier")String referenceIdentifier,
            @Param("status") TransactionStatus status);

    @Query("select authorize from AuthorizeTransaction authorize " +
            "where authorize.uuid=:referenceIdentifier " +
            "and authorize.status=:status ")
    AuthorizeTransaction findAuthorizeTransactionByRefId(
            @Param("referenceIdentifier")String referenceIdentifier,
            @Param("status") TransactionStatus status);

    @Modifying
    @Transactional
    @Query(value = "delete from Transaction where timestamp<:thanHour")
    void deleteTranOlderThanHour(@Param("thanHour")Timestamp thanHour);

}
