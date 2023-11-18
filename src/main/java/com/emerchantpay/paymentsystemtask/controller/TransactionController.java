package com.emerchantpay.paymentsystemtask.controller;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.exceptions.MerchantException;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.service.TransactionService;
import com.emerchantpay.paymentsystemtask.service.handler.TransactionHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;

@RestController
public class TransactionController {
    @Autowired
    TransactionHandlerService transactionHandlerService;

    @Autowired
    TransactionService transactionService;


    @GetMapping("/transactions")
    public ModelAndView findTransaction() {


        Set<TransactionDto> transactions = transactionService.findAuthenticatedUserTransactions();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("transactions", transactions);
        modelAndView.setViewName("transaction/transaction");

        return modelAndView;
    }
    @PostMapping("/transactions")
    public List<TransactionDto> importTransactions(@RequestBody List<TransactionDto> trans) throws MerchantException, TransactionException {
       return transactionHandlerService.handleTransactions(trans);
    }
}
