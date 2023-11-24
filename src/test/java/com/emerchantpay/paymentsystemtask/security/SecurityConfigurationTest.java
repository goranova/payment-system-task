package com.emerchantpay.paymentsystemtask.security;

import com.emerchantpay.paymentsystemtask.PaymentsUtils;
import com.emerchantpay.paymentsystemtask.dao.UserAccountRepository;
import com.emerchantpay.paymentsystemtask.model.UserAccount;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SecurityConfigurationTest {

    private UserAccount mer;
    private UserAccount adm;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private UserAccountRepository rep;


    @BeforeAll
    public void init(){
         mer = new UserAccount("MerchantUserTest",
                "user@merchant.com",
                "MERCHANT",
                "$2a$10$bH6fi57oRN8dF51apQIiW.ph5nbjwBsYLH71rUNnlDvv4KGI4WDqi");
         adm = new UserAccount("AdminUserTest",
                "user@admin.com",
                "ADMIN",
                "$2a$10$bH6fi57oRN8dF51apQIiW.ph5nbjwBsYLH71rUNnlDvv4KGI4WDqi");

        rep.saveAll(List.of(mer,adm));
    }


    @Test
    public void getTransactionsMerchantUserTest_success()  {
        String password= PaymentsUtils.decodeTestUserPassword();
        ResponseEntity<String> result  = template.withBasicAuth(mer.getUserName(), password)
                .getForEntity("/transactions", String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void getTransactionsAdminUserTest_forbidden()  {
        String password= PaymentsUtils.decodeTestUserPassword();
        ResponseEntity<String> result  = template.withBasicAuth(adm.getUserName(), password)
                .getForEntity("/transactions", String.class);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void getMerchantsAdminUserTest_success()  {
        String password= PaymentsUtils.decodeTestUserPassword();
        ResponseEntity<String> result  = template.withBasicAuth(adm.getUserName(), password)
                .getForEntity("/merchants", String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void getMerchantsMerchUserTest_forbidden()  {
        String password= PaymentsUtils.decodeTestUserPassword();
        ResponseEntity<String> result  = template.withBasicAuth(mer.getUserName(), password)
                .getForEntity("/merchants", String.class);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @AfterAll
    public void destroy(){
        if(adm!=null && adm.getIdentity()!=null){
            rep.deleteById(adm.getIdentity());
        }
        if (mer!=null && mer.getIdentity()!=null){
            rep.deleteById(mer.getIdentity());
        }
    }
}
