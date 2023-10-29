package com.emerchantpay.paymentsystemtask.service;


import com.emerchantpay.paymentsystemtask.dao.UserAccountRepository;
import com.emerchantpay.paymentsystemtask.dto.UserAccountDto;
import com.emerchantpay.paymentsystemtask.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService implements UserDetailsService {

	@Autowired
	UserAccountRepository userAccRep;

	public UserAccount getUserByName(String name){
		return  userAccRep.findUserByName(name);
	}



	public UserAccount save(UserAccount userAccount) {
		return userAccRep.save(userAccount);
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount user = userAccRep.findUserByName(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		UserAccountDto userAccountDto = new UserAccountDto(user);
		return userAccountDto;
	}
}
