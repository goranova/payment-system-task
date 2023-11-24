package com.emerchantpay.paymentsystemtask.dao;

import com.emerchantpay.paymentsystemtask.PaymentsUtils;
import com.emerchantpay.paymentsystemtask.model.Merchant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MerchantRepositoryTest {

    @Autowired
    private MerchantRepository merchantRepository;


  @Test
    public void findAllTest() {

        Merchant merchant = PaymentsUtils.createMerchant();
        Merchant mer =  merchantRepository.save(merchant);
        List<Merchant> merchants = merchantRepository.findAll();
        assertTrue(merchants.size()>=1);
        merchantRepository.deleteById(mer.getIdentifier());
    }

    @Test
    public void findByIdTest_success() {

        Merchant merchant=PaymentsUtils.createMerchant();
        merchantRepository.save(merchant);
        Optional<Merchant> foundMerchant = merchantRepository.findById(merchant.getIdentifier());
        assertTrue(foundMerchant.isPresent());
        merchantRepository.deleteById(merchant.getIdentifier());
    }

    @Test
    public void findByIdTest_fail() {
        Optional<Merchant> foundMerchant = merchantRepository.findById(1L);
        assertFalse(foundMerchant.isPresent());
    }

    @Test
    public void findByDescriptionStatusTest_success() {
        Merchant merchant = PaymentsUtils.createMerchant();
        merchantRepository.save(merchant);
        Merchant foundMerchant =
                merchantRepository.findMerchantByDescrStatus(merchant.getDescription(), merchant.getStatus());
        assertNotNull(foundMerchant);
        merchantRepository.deleteById(merchant.getIdentifier());
    }

    @Test
    public void findByDescriptionStatusTest_fail() {
        Merchant merchant = PaymentsUtils.createMerchant();
        Merchant foundMerchant =
                merchantRepository.findMerchantByDescrStatus(merchant.getDescription(), merchant.getStatus());
        assertNull(foundMerchant);
    }
}



