package com.emerchantpay.paymentsystemtask.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    @GetMapping("/successAlert")
    public ModelAndView successAlert() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("alerts/successAlert");
        return modelAndView;
    }

    @GetMapping("/errorAlert")
    public ModelAndView errorAlert() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("alerts/errorAlert");
        return modelAndView;
    }

}
