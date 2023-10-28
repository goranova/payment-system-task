package com.emerchantpay.paymentsystemtask.controller;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.service.handler.TransactionHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    TransactionHandlerService transactionService;

    @GetMapping("/display")
    public ModelAndView findAllTransaction() {
        List<TransactionDto> transactions = transactionService.getAllTransactions();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("transactions", transactions);
        modelAndView.setViewName("transaction/transaction-display");

        return modelAndView;
    }
    @PostMapping("/importTransaction")
    public List<TransactionDto> importTransactions(@RequestBody List<TransactionDto> trans){
       return transactionService.handleTransactionChain(trans);

    }

}
