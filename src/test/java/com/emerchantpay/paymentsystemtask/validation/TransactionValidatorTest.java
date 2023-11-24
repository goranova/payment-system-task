package com.emerchantpay.paymentsystemtask.validation;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.enums.TransactionStatus;
import com.emerchantpay.paymentsystemtask.enums.TransactionType;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.validation.transaction.AuthorizeValidator;
import com.emerchantpay.paymentsystemtask.validation.transaction.ChargeValidator;
import com.emerchantpay.paymentsystemtask.validation.transaction.ReversalValidator;
import com.emerchantpay.paymentsystemtask.validation.transaction.TransactionValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionValidatorTest {

    @Test
    public void validateSupportedStatusTest_success()  {
        TransactionValidator validator = new AuthorizeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf53",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.AUTHORIZE.getName(),
                        "tsvety@gmail.com",
                        " +359884719125",
                        250.0,
                        null,
                        null);
        assertDoesNotThrow(()->validator.validateSupportedStatus(trans));
    }

    @Test
    public void validateSupportedStatusTest_throwsException_null() {
        TransactionValidator validator = new AuthorizeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf53",
                        null,
                        TransactionType.AUTHORIZE.getName(),
                        "tsvety@gmail.com",
                        " +359884719125",
                        250.0,
                        null,
                        null);
        assertThrows(TransactionException.class,()->validator.validateSupportedStatus(trans));
    }

    @Test
    public void validateSupportedStatusTest_throwsException() {
        TransactionValidator validator = new AuthorizeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf53",
                        "APPR",
                        TransactionType.AUTHORIZE.getName(),
                        "tsvety@gmail.com",
                        " +359884719125",
                        250.0,
                        null,
                        null);
        assertThrows(TransactionException.class,()->validator.validateSupportedStatus(trans));
    }

    @Test
    public void validateStatusTest_success() {
        TransactionValidator validator = new AuthorizeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf53",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.REFUND.getName(),
                        "tsvety@gmail.com",
                        " +359884719125",
                        250.0,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf45",
                        null);

        validator.validateStatus(trans);
        assertEquals(TransactionStatus.APPROVED.getName(),trans.getStatus());
    }

    @Test
    public void validateStatusTest_error() {
        TransactionValidator validator = new ChargeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf53",
                        TransactionStatus.REFUNDED.getName(),
                        TransactionType.REFUND.getName(),
                        "tsvety@gmail.com",
                        " +359884719125",
                        250.0,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf45",
                        null);

        validator.validateStatus(trans);
        assertEquals(TransactionStatus.ERROR.getName(),trans.getStatus());

    }

    @Test
    public void validateUuidTest_valid() {
        TransactionValidator validator = new ReversalValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf46",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.REVERSAL.getName(),
                        "tsvety@gmail.com",
                        " +359884719125",
                        null,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf45",
                        null);

        String inputUuid = trans.getUuid();
        validator.validateUuid(trans);
        assertEquals(inputUuid,trans.getUuid());
    }

    @Test
    public void validateUuidTest_null() {
        TransactionValidator validator = new ReversalValidator();
        TransactionDto trans =
                new TransactionDto(null,
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.REVERSAL.getName(),
                        "tsvety@gmail.com",
                        " +359884719125",
                        null,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf45",
                        null);

        validator.validateUuid(trans);
        assertNotNull(trans.getUuid());
    }


    @Test
    public void validateUuidTest_notValid() {
        TransactionValidator validator = new ReversalValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.REVERSAL.getName(),
                        "tsvety@gmail.com",
                        " +359884719125",
                        null,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf45",
                        null);

        String inputUuid = trans.getUuid();
        validator.validateUuid(trans);
        assertNotNull(trans.getUuid());
        assertNotEquals(inputUuid,trans.getUuid());
    }


    @Test
    public void validateEmailTest_valid() {
        TransactionValidator validator = new ChargeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf46",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.CHARGE.getName(),
                        "tsvety@gmail.com",
                        " +359884719125",
                        20.0,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf45",
                        null);

        assertDoesNotThrow(()->validator.validateEmail(trans));
    }

    @Test
    public void validateEmailTest_null() {
        TransactionValidator validator = new ChargeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf46",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.CHARGE.getName(),
                        null,
                        " +359884719125",
                        20.0,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf45",
                        null);

        assertThrows(TransactionException.class,()->validator.validateEmail(trans));
    }

    @Test
    public void validateEmailTest_notValid() {
        TransactionValidator validator = new ChargeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf46",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.CHARGE.getName(),
                        "tsvety@",
                        " +359884719125",
                        20.0,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf45",
                        null);

        assertThrows(TransactionException.class,()->validator.validateEmail(trans));
    }

    @Test
    public void validateAmountTest_valid() {
        TransactionValidator validator = new ChargeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf46",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.CHARGE.getName(),
                        "tsvety@gmail.com",
                        " +359884719125",
                        20.0,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf45",
                        null);

        assertDoesNotThrow(()->validator.validateAmount(trans));
    }


    @Test
    public void validateAmountTest_null() {
        TransactionValidator validator = new ChargeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf46",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.CHARGE.getName(),
                        "tsvety@gmail.com",
                        " +359884719125",
                        null,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf45",
                        null);

        assertThrows(TransactionException.class,()->validator.validateAmount(trans));
    }


    @Test
    public void validateAmountTest_notValid() {
        TransactionValidator validator = new ChargeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf46",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.CHARGE.getName(),
                        "tsvety@gmail.com",
                        " +359884719125",
                        -20.0,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf45",
                        null);

        assertThrows(TransactionException.class,()->validator.validateAmount(trans));
    }


    @Test
    public void validateAuthRefIdTest_valid() throws TransactionException {
        TransactionValidator validator = new AuthorizeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf46",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.AUTHORIZE.getName(),
                        "tsvety@gmail.com",
                        " +359884719125",
                        20.0,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf45",
                        null);
        validator.validateReferenceId(trans);
        assertNull(trans.getReferenceIdentifier());
    }

    @Test
    public void validateErrorRefIdTest_valid() throws TransactionException {
        TransactionValidator validator = new ChargeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf46",
                        TransactionStatus.ERROR.getName(),
                        TransactionType.CHARGE.getName(),
                        "tsvety@gmail.com",
                        " +359884719125",
                        20.0,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf45",
                        null);
        validator.validateReferenceId(trans);
        assertNull(trans.getReferenceIdentifier());
    }

    @Test
    public void validateRefIdTest_throwException() {
        TransactionValidator validator = new ChargeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf46",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.CHARGE.getName(),
                        "tsvety@gmail.com",
                        " +359884719125",
                        20.0, null,
                        null);
        assertThrows(TransactionException.class,()->validator.validateReferenceId(trans));
    }

    @Test
    public void validatePhoneTest_valid() {
        TransactionValidator validator = new ChargeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf46",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.CHARGE.getName(),
                        "tsvety@gmail.com",
                        "+359884719125",
                        20.0,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf45",
                        null);

        assertDoesNotThrow(()->validator.validatePhone(trans));
    }

    @Test
    public void validatePhoneTest_null() {
        TransactionValidator validator = new ChargeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf46",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.CHARGE.getName(),
                        "tsvety@gmail.com",
                        null,
                        20.0,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf45",
                        null);

        assertThrows(TransactionException.class,()->validator.validatePhone(trans));
    }

    @Test
    public void validatePhoneTest_notValid() {
        TransactionValidator validator = new ChargeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf46",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.CHARGE.getName(),
                        "tsvety@gmail.com",
                        " +3598notValid",
                        20.0,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf45",
                        null);

        assertThrows(TransactionException.class,()->validator.validatePhone(trans));
    }

    @Test
    public void validateTransactionTest_authValidator() throws TransactionException {
        TransactionValidator validator = new AuthorizeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf53",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.AUTHORIZE.getName(),
                        "tsvety@gmail.com",
                        "+359884719125",
                        250.0,
                        null,
                        null);
        TransactionDto validateTransaction = validator.validateTransaction(trans);

        assertEquals(trans.getUuid(), validateTransaction.getUuid());
        assertEquals(trans.getStatus(), validateTransaction.getStatus());
        assertDoesNotThrow(()->validator.validateTransaction(trans));
    }

    @Test
    public void validateTransactionTest_chargeValidator() throws TransactionException {
        TransactionValidator validator = new AuthorizeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf53",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.CHARGE.getName(),
                        "tsvety@gmail.com",
                        "+359884719125",
                        250.0,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf52",
                        null);
        TransactionDto validateTransaction = validator.validateTransaction(trans);

        assertEquals(trans.getUuid(), validateTransaction.getUuid());
        assertEquals(trans.getStatus(), validateTransaction.getStatus());
        assertDoesNotThrow(()->validator.validateTransaction(trans));
    }

    @Test
    public void validateTransactionTest_refundValidator() throws TransactionException {
        TransactionValidator validator = new AuthorizeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf53",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.REFUND.getName(),
                        "tsvety@gmail.com",
                        "+359884719125",
                        250.0,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf52",
                        null);
        TransactionDto validateTransaction = validator.validateTransaction(trans);

        assertEquals(trans.getUuid(), validateTransaction.getUuid());
        assertEquals(trans.getStatus(), validateTransaction.getStatus());
        assertDoesNotThrow(()->validator.validateTransaction(trans));
    }

    @Test
    public void validateTransactionTest_reversalValidator() throws TransactionException {
        TransactionValidator validator = new AuthorizeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf53",
                        TransactionStatus.APPROVED.getName(),
                        TransactionType.REVERSAL.getName(),
                        "tsvety@gmail.com",
                        "+359884719125",
                        null,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf52",
                        null);

        TransactionDto validateTransaction = validator.validateTransaction(trans);
        assertEquals(trans.getUuid(), validateTransaction.getUuid());
        assertEquals(trans.getStatus(), validateTransaction.getStatus());
        assertDoesNotThrow(()->validator.validateTransaction(trans));
    }

    @Test
    public void validateTransactionTest_reversalValidator_throwsException() throws TransactionException {
        TransactionValidator validator = new AuthorizeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf53",
                        TransactionStatus.APPROVED.getName(),
                        "Revr",
                        "tsvety@gmail.com",
                        "+359884719125",
                        null,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf52",
                        null);

       assertThrows(TransactionException.class,()->validator.validateTransaction(trans));
    }

    @Test
    public void validateTransactionTest_throwsException_null(){
        TransactionValidator validator = new AuthorizeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf53",
                        TransactionStatus.APPROVED.getName(),
                        null,
                        "tsvety@gmail.com",
                        "+359884719125",
                        250.0,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf52",
                        null);

        assertThrows(TransactionException.class, ()->validator.validateTransaction(trans));
    }

    @Test
    public void validateTransactionTest_throwsException() {
        TransactionValidator validator = new AuthorizeValidator();
        TransactionDto trans =
                new TransactionDto("a404d5c4-1b49-40fa-9d0e-d36e500bdf53",
                        TransactionStatus.APPROVED.getName(),
                        "Auth",
                        "tsvety@gmail.com",
                        "+359884719125",
                        250.0,
                        "a404d5c4-1b49-40fa-9d0e-d36e500bdf52",
                        null);
        assertThrows(TransactionException.class, ()->validator.validateTransaction(trans));
    }

    @Test
    public void validateTest_null(){

        TransactionValidator validator = new AuthorizeValidator();
        assertThrows(TransactionException.class, ()->validator.validate(null));
    }
}
