package com.emerchantpay.paymentsystemtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PaymentSystemTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentSystemTaskApplication.class, args);
	}

}
