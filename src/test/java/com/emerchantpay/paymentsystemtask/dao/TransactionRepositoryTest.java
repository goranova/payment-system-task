package com.emerchantpay.paymentsystemtask.dao;

import com.emerchantpay.paymentsystemtask.PaymentsUtils;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.model.Merchant;
import com.emerchantpay.paymentsystemtask.model.transaction.AuthorizeTransaction;
import com.emerchantpay.paymentsystemtask.model.transaction.ChargeTransaction;
import com.emerchantpay.paymentsystemtask.model.transaction.Transaction;
import com.emerchantpay.paymentsystemtask.utils.TransactionUtils;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionRepositoryTest {

    private final Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private TransactionRepository transRepository;

    @Autowired
    private  MerchantRepository merchantRepository;


    @Test
    public void findAuthorizeTransactionByRefIdMerId_success(){

        Merchant mer = PaymentsUtils.createMerchant();
        Transaction authTrans = PaymentsUtils.setTransactionProperties(TransactionType.AUTHORIZE,null, mer);
        Transaction chTrans =  PaymentsUtils.setTransactionProperties(TransactionType.CHARGE, authTrans.getUuid(), authTrans.getMerchant());
        try {
            merchantRepository.save(mer);
            transRepository.saveAll(List.of(authTrans, chTrans));

            AuthorizeTransaction auth =
                    transRepository.findAuthorizeTransactionByRefIdMerId(chTrans.getReferenceIdentifier(),
                            chTrans.getStatus(),
                            String.valueOf(chTrans.getMerchant().getIdentifier()));
            assertNotNull(auth);
        } catch (Exception e){
            log.error(e.getMessage());
            fail(e.getMessage());
        } finally {
            transRepository.deleteAllById(List.of(chTrans.getUuid(),authTrans.getUuid()));
            if(mer.getIdentifier()!=null){
                merchantRepository.deleteById(mer.getIdentifier());
            }
        }
    }

    @Test
    public void findAuthorizeTransactionByRefIdMerIdTest_fail(){

        AuthorizeTransaction auth =
                transRepository.findAuthorizeTransactionByRefIdMerId(TransactionUtils.generateRandomUuid(),
                        TransactionStatus.APPROVED,
                        "1");
        assertNull(auth);
    }

    @Test
    public void findChargeTransactionByRefIdMerIdTest_success(){

        Merchant mer = PaymentsUtils.createMerchant();
        Transaction authTrans =  PaymentsUtils.setTransactionProperties(TransactionType.AUTHORIZE,null,mer);
        Transaction chTrans =  PaymentsUtils.setTransactionProperties(TransactionType.CHARGE,authTrans.getUuid(),authTrans.getMerchant());
        Transaction refTrans = PaymentsUtils.setTransactionProperties(TransactionType.REFUND,chTrans.getReferenceIdentifier(),chTrans.getMerchant());

        List<Transaction> trans = List.of(authTrans,chTrans,refTrans);
        try {
            merchantRepository.save(mer);
            transRepository.saveAll(trans);

            ChargeTransaction ch =
                    transRepository.findChargeTransactionByRefIdMerId(refTrans.getReferenceIdentifier(),
                            refTrans.getStatus(),
                            String.valueOf(refTrans.getMerchant().getIdentifier()));
            assertNotNull(ch);

        } catch (Exception e){
            log.error(e.getMessage());
            fail(e.getMessage());
        } finally {
            transRepository.deleteAllById(List.of(refTrans.getUuid(),chTrans.getUuid(),authTrans.getUuid()));
            if(mer.getIdentifier()!=null){
                merchantRepository.deleteById(mer.getIdentifier());
            }
        }
    }

    @Test
    public void findChargeTransactionByRefIdMerIdTest_fail(){

        ChargeTransaction ch =
                transRepository.findChargeTransactionByRefIdMerId(TransactionUtils.generateRandomUuid(),
                        TransactionStatus.APPROVED,
                        "1");
        assertNull(ch);

    }

    @Test
    public void existsByTransactionTypeAndReferenceIdentifier_success(){

        Transaction authTrans = PaymentsUtils.setTransactionProperties(TransactionType.AUTHORIZE,null,null);
        Transaction chTrans = PaymentsUtils.setTransactionProperties(TransactionType.CHARGE,authTrans.getUuid(),null);
        transRepository.saveAll(List.of(authTrans,chTrans));

        boolean isTransExist
                = transRepository.existsByTransactionTypeAndReferenceIdentifier(chTrans.getTransactionType(),
                                                                                 chTrans.getReferenceIdentifier());
        assertTrue(isTransExist);
        transRepository.deleteAllById(List.of(chTrans.getUuid(),authTrans.getUuid()));
    }


    @Test
    public void existsByTransactionTypeAndReferenceIdentifier_fail(){
        String refId= TransactionUtils.generateRandomUuid();

        boolean isTransExist
                = transRepository.existsByTransactionTypeAndReferenceIdentifier(TransactionType.CHARGE.getName(),
                refId);
        assertFalse(isTransExist);
    }

    @Test
    public void deleteTranOlderThanHourTest_success(){

        Transaction authTrans = PaymentsUtils.setTransactionProperties(TransactionType.AUTHORIZE,null, null);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -2);
        Timestamp time=authTrans.getTimestamp();
        time.setTime(calendar.getTimeInMillis());
        transRepository.save(authTrans);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        transRepository.deleteTranOlderThanHour(timestamp);
        assertFalse(transRepository.findById(authTrans.getUuid()).isPresent());
    }

    @Test
    public void deleteTranOlderThanHourTest_fail() {

        Transaction authTrans = PaymentsUtils.setTransactionProperties(TransactionType.AUTHORIZE,null,null);

        transRepository.save(authTrans);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -2);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        timestamp.setTime(calendar.getTimeInMillis());

        transRepository.deleteTranOlderThanHour(timestamp);
        assertTrue(transRepository.findById(authTrans.getUuid()).isPresent());
        transRepository.deleteById(authTrans.getUuid());
    }
}
