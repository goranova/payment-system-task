package com.emerchantpay.paymentsystemtask.controller;

import com.emerchantpay.paymentsystemtask.dto.TransactionDto;
import com.emerchantpay.paymentsystemtask.error.MissingMerchantException;
import com.emerchantpay.paymentsystemtask.service.TransactionMerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    TransactionMerchantService transMerService;


    @GetMapping("/display")
    public ModelAndView findTransaction() {
        List<TransactionDto> transactions = transMerService.findTransactions();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("transactions", transactions);
        modelAndView.setViewName("transaction/transaction-display");

        return modelAndView;
    }
    @PostMapping("/importTransaction")
    public List<TransactionDto> importTransactions(@RequestBody List<TransactionDto> trans) throws MissingMerchantException {
       return transMerService.handleTransactionChain(trans);

    }

}
