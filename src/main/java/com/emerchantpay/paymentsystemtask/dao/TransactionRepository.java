package com.emerchantpay.paymentsystemtask.dao;

import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.model.transaction.AuthorizeTransaction;
import com.emerchantpay.paymentsystemtask.model.transaction.ChargeTransaction;
import com.emerchantpay.paymentsystemtask.model.transaction.RefundTransaction;
import com.emerchantpay.paymentsystemtask.model.transaction.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query("select authorize from AuthorizeTransaction authorize")
    List<AuthorizeTransaction> findAuthorizeTransactions();

    @Query("select charge from ChargeTransaction charge")
    List<ChargeTransaction> findChargeTransactions();

    @Query("select charge from ChargeTransaction charge " +
            "where charge.referenceIdentifier=:referenceIdentifier " +
            "and charge.status=:status")
    ChargeTransaction findChargeTransactionByRefId(
            @Param("referenceIdentifier")String referenceIdentifier,
            @Param("status") TransactionStatus status);

    @Query("select authorize from AuthorizeTransaction authorize " +
            "where authorize.uuid=:referenceIdentifier " +
            "and authorize.status=:status ")
    AuthorizeTransaction findAuthorizeTransactionByRefId(
            @Param("referenceIdentifier")String referenceIdentifier,
            @Param("status") TransactionStatus status);

    @Query("select refund from RefundTransaction refund")
    List<RefundTransaction> findRefundTransactions();

    @Query("select reversal from ReversalTransaction reversal")
    List<RefundTransaction> findReversalTransactions();

    @Modifying
    @Transactional
    @Query(value = "delete from Transaction where timestamp<:thanHour")
    void deleteTranOlderThanHour(@Param("thanHour")Timestamp thanHour);

}
