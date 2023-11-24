package com.emerchantpay.paymentsystemtask.validation;

import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.enums.MerchantStatus;
import com.emerchantpay.paymentsystemtask.exceptions.MerchantException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MerchantValidatorTest {

    @Test
    public void validateSupportedStatusTest_valid() {
        MerchantDto merchant = new MerchantDto("Lidl Bulgaria",
                                             "lid@lidl.com",
                                             MerchantStatus.ACTIVE.getName(),
                               2500.0 );
        MerchantValidator validator = new MerchantValidator();
        assertDoesNotThrow(()->validator.validateSupportedStatus(merchant));
    }

    @Test
    public void validateSupportedStatusTest_null() {
        MerchantDto merchant = new MerchantDto("Lidl Bulgaria",
                "lid@lidl.com",
                null,
                2500.0 );
        MerchantValidator validator = new MerchantValidator();
        assertThrows(MerchantException.class,()->validator.validateSupportedStatus(merchant));
    }

    @Test
    public void validateSupportedStatusTest_notValid()  {
        MerchantDto merchant = new MerchantDto("Lidl Bulgaria",
                "lid@lidl.com",
                "AV",
                2500.0 );
        MerchantValidator validator = new MerchantValidator();
        assertThrows(MerchantException.class,()->validator.validateSupportedStatus(merchant));
    }


    @Test
    public void validateEmailTest_valid()  {
        MerchantDto merchant = new MerchantDto("Lidl Bulgaria",
                "lid@lidl.com",
                MerchantStatus.ACTIVE.getName(),
                2500.0 );
        MerchantValidator validator = new MerchantValidator();
        assertDoesNotThrow(()->validator.validateEmail(merchant));
    }

    @Test
    public void validateEmailTest_null()  {
        MerchantDto merchant = new MerchantDto("Lidl Bulgaria",
                null,
                MerchantStatus.ACTIVE.getName(),
                2500.0 );
        MerchantValidator validator = new MerchantValidator();
        assertThrows(MerchantException.class,()->validator.validateEmail(merchant));
    }

    @Test
    public void validateEmailTest_notValid()  {
        MerchantDto merchant = new MerchantDto("Lidl Bulgaria",
                "lidl.com",
                MerchantStatus.ACTIVE.getName(),
                2500.0 );
        MerchantValidator validator = new MerchantValidator();
        assertThrows(MerchantException.class,()->validator.validateEmail(merchant));
    }

    @Test
    public void validateTotalTransSumTest_valid()  {
        MerchantDto merchant = new MerchantDto("Lidl Bulgaria",
                "lid@lidl.com",
                MerchantStatus.ACTIVE.getName(),
                2500.0 );
        MerchantValidator validator = new MerchantValidator();
        assertDoesNotThrow(()->validator.validateTotalTrSum(merchant));
    }

    @Test
    public void validateTotalTransSumTest_null()  {
        MerchantDto merchant = new MerchantDto("Lidl Bulgaria",
                "lid@lidl.com",
                MerchantStatus.ACTIVE.getName(),
                null );
        MerchantValidator validator = new MerchantValidator();
        assertThrows(MerchantException.class, ()->validator.validateTotalTrSum(merchant));
    }

    @Test
    public void validateTotalTransSumTest_notValid()  {
        MerchantDto merchant = new MerchantDto("Lidl Bulgaria",
                "lid@lidl.com",
                MerchantStatus.ACTIVE.getName(),
                -2500.0 );
        MerchantValidator validator = new MerchantValidator();
        assertThrows(MerchantException.class, ()->validator.validateTotalTrSum(merchant));
    }

    @Test
    public void validateMerchantDescrTest_valid()  {
        MerchantDto merchant = new MerchantDto("Lidl Bulgaria",
                "lid@lidl.com",
                MerchantStatus.ACTIVE.getName(),
                2500.0 );
        MerchantValidator validator = new MerchantValidator();
        assertDoesNotThrow(()->validator.validateDescription(merchant));
    }

    @Test
    public void validateMerchantDescrTest_null()  {
        MerchantDto merchant = new MerchantDto(null,
                "lid@lidl.com",
                MerchantStatus.ACTIVE.getName(),
                2500.0 );
        MerchantValidator validator = new MerchantValidator();
        assertThrows(MerchantException.class,()->validator.validateDescription(merchant));
    }

    @Test
    public void validateTest_valid()  {
        MerchantDto merchant = new MerchantDto("Lidl Bulgaria",
                "lid@lidl.com",
                MerchantStatus.ACTIVE.getName(),
                2500.0 );
        MerchantValidator validator = new MerchantValidator();
        assertDoesNotThrow(()->validator.validate(merchant));
    }

    @Test
    public void validateTest_null()  {
        MerchantValidator validator = new MerchantValidator();
        assertThrows(MerchantException.class,()->validator.validate(null));
    }
}
