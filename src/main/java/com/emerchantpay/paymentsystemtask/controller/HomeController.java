package com.emerchantpay.paymentsystemtask.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/payments")
public class HomeController {

    @GetMapping()
    public ModelAndView paymentsHome() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/home/home");
        return modelAndView;
    }
}
