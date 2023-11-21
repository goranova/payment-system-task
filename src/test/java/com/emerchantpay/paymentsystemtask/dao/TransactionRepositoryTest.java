package com.emerchantpay.paymentsystemtask.dao;

import com.emerchantpay.paymentsystemtask.PaymentsUtils;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.model.Merchant;
import com.emerchantpay.paymentsystemtask.model.transaction.AuthorizeTransaction;
import com.emerchantpay.paymentsystemtask.model.transaction.ChargeTransaction;
import com.emerchantpay.paymentsystemtask.model.transaction.Transaction;
import com.emerchantpay.paymentsystemtask.utils.TransactionUtils;
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

    @Autowired
    private TransactionRepository transRepository;

    @Autowired
    private  MerchantRepository merchantRepository;


    @Test
    public void findAuthorizeTransactionByRefIdMerId_success(){

        Merchant mer = PaymentsUtils.createMerchant();
        merchantRepository.save(mer);

        PaymentsUtils paymentsUtils=new PaymentsUtils();
        Transaction authTrans =
                paymentsUtils.createTransaction(TransactionType.AUTHORIZE.getName(),null,mer);
        Transaction chargeTransaction =
                paymentsUtils.createTransaction(TransactionType.CHARGE.getName(), authTrans.getUuid(),mer);
        List<Transaction> trans =List.of(authTrans,chargeTransaction);
        transRepository.saveAll(trans);

        AuthorizeTransaction auth =
                transRepository.findAuthorizeTransactionByRefIdMerId(chargeTransaction.getReferenceIdentifier(),
                        chargeTransaction.getStatus(),
                        String.valueOf(chargeTransaction.getMerchant().getIdentifier()));
        assertNotNull(auth);

        transRepository.deleteAllById(List.of(chargeTransaction.getUuid(),authTrans.getUuid()));
        merchantRepository.deleteById(mer.getIdentifier());
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
        merchantRepository.save(mer);

        PaymentsUtils paymentsUtils=new PaymentsUtils();
        Transaction authTrans =
                paymentsUtils.createTransaction(TransactionType.AUTHORIZE.getName(),null,mer);
        Transaction chargeTransaction =
                paymentsUtils.createTransaction(TransactionType.CHARGE.getName(), authTrans.getUuid(),authTrans.getMerchant());
        Transaction refundTrans=
                paymentsUtils.createTransaction(TransactionType.REFUND.getName(), chargeTransaction.getReferenceIdentifier(),chargeTransaction.getMerchant());
        List<Transaction> trans =List.of(authTrans,chargeTransaction,refundTrans);
        transRepository.saveAll(trans);

        ChargeTransaction ch =
                transRepository.findChargeTransactionByRefIdMerId(refundTrans.getReferenceIdentifier(),
                        refundTrans.getStatus(),
                        String.valueOf(refundTrans.getMerchant().getIdentifier()));
        assertNotNull(ch);

        transRepository.deleteAllById(List.of(refundTrans.getUuid(),chargeTransaction.getUuid(),authTrans.getUuid()));
        merchantRepository.deleteById(mer.getIdentifier());

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
        PaymentsUtils paymentsUtils=new PaymentsUtils();
        Transaction authTrans =
                paymentsUtils.createTransaction(TransactionType.AUTHORIZE.getName(),null,null);

        Transaction chargeTransaction =
                paymentsUtils.createTransaction(TransactionType.CHARGE.getName(), authTrans.getUuid(),authTrans.getMerchant());

        transRepository.saveAll(List.of(authTrans,chargeTransaction));
        boolean isTransExist
                = transRepository.existsByTransactionTypeAndReferenceIdentifier(chargeTransaction.getTransactionType(),
                                                                                 chargeTransaction.getReferenceIdentifier());
        assertTrue(isTransExist);
        transRepository.deleteAllById(List.of(chargeTransaction.getUuid(),authTrans.getUuid()));
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

        PaymentsUtils paymentsUtils=new PaymentsUtils();
        Transaction authTrans =
                paymentsUtils.createTransaction(TransactionType.AUTHORIZE.getName(),null,null);

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

        PaymentsUtils paymentsUtils=new PaymentsUtils();
        Transaction authTrans =
                paymentsUtils.createTransaction(TransactionType.AUTHORIZE.getName(), null, null);

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
