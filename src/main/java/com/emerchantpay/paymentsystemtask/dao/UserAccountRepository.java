package com.emerchantpay.paymentsystemtask.dao;


import com.emerchantpay.paymentsystemtask.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
	
	@Override
    List<UserAccount> findAll();

    @Query("select user from UserAccount user where user.userName=:name")
    UserAccount findUserByName (@Param("name") String name);

}
