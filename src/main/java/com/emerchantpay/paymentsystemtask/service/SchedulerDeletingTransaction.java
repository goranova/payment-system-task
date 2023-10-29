package com.emerchantpay.paymentsystemtask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Calendar;

@Component
public class SchedulerDeletingTransaction {
    @Autowired
    TransactionService service;

    @Scheduled(cron = "0 0 */1 ? * *")
    public void deleteTransactions() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -1);
        timestamp.setTime(calendar.getTimeInMillis());
        service.deleteTransOlderThanHour(timestamp);

    }
}
