package com.emerchantpay.paymentsystemtask.dao;

import com.emerchantpay.paymentsystemtask.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant,Long> {
    @Override
    List<Merchant> findAll();
    @Override
    Optional<Merchant> findById(Long id);
}
