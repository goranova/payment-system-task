package com.emerchantpay.paymentsystemtask.service;

import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.enums.MerchantStatus;
import com.emerchantpay.paymentsystemtask.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
   private static final String MERCHANT="MERCHANT";
    @Autowired
    UserAccountService userAccountService;
    @Autowired
    MerchantService merchantService;
    public MerchantDto getAuthenticatedMerchant() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        UserAccount user = userAccountService.getUserByName(userName);

        if (user.getRole().equals(MERCHANT)){
           MerchantDto merchant
                   =  merchantService.findMerchantByDescrStatus(user.getUserName(), MerchantStatus.ACTIVE.getName());
           return merchant;
        }
        return null;
    }


}
