package com.emerchantpay.paymentsystemtask.dao;


import com.emerchantpay.paymentsystemtask.PaymentsUtils;
import com.emerchantpay.paymentsystemtask.model.UserAccount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAccountRepositoryTest {
    @Autowired
    private UserAccountRepository userAccountRepository;


    @Test
    public void findAllTest() {
        UserAccount user = PaymentsUtils.createUser();
        userAccountRepository.save(user);
        List<UserAccount> users = userAccountRepository.findAll();
        assertTrue(users.size()>=1);
        userAccountRepository.deleteById(user.getIdentity());
    }

    @Test
    public void findByUserName_success(){
        UserAccount user = PaymentsUtils.createUser();
        userAccountRepository.save(user);
        assertNotNull(userAccountRepository.findUserByName(user.getUserName()));

    }

    @Test
    public void findByUserName_fail(){
        UserAccount user = PaymentsUtils.createUser();
        assertNull(userAccountRepository.findUserByName(user.getUserName()));
    }
}
