package com.emerchantpay.paymentsystemtask.dao;

import com.emerchantpay.paymentsystemtask.enums.MerchantStatus;
import com.emerchantpay.paymentsystemtask.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant,Long> {
    @Override
    List<Merchant> findAll();
    @Override
    Optional<Merchant> findById(Long id);

    @Query("select merchant from Merchant merchant " +
            "where merchant.description=:description " +
            "and merchant.status=:status " )
    Merchant findMerchantByDescrStatus(
            @Param("description") String description,
            @Param("status") MerchantStatus status
    );
}
