package com.emerchantpay.paymentsystemtask.controller;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.exceptions.MerchantException;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.service.TransactionMerchantHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    TransactionMerchantHandlerService transMerService;


    @GetMapping("/display")
    public ModelAndView findTransaction() {
        List<TransactionDto> transactions = transMerService.findTransactions();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("transactions", transactions);
        modelAndView.setViewName("transaction/transaction-display");

        return modelAndView;
    }
    @PostMapping("/importTransaction")
    public List<TransactionDto> importTransactions(@RequestBody List<TransactionDto> trans) throws MerchantException, TransactionException {
       return transMerService.handleTransactions(trans);

    }

}
