package com.emerchantpay.paymentsystemtask.controller;

import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.exceptions.MerchantException;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.service.MerchantService;
import com.emerchantpay.paymentsystemtask.service.TransactionMerchantHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/merchants")
@SessionAttributes("existingMerchant")
@Scope("session")
public class MerchantController {

    @Autowired
    MerchantService merchantService;

    @Autowired
    TransactionMerchantHandlerService merTransService;

    @GetMapping("/display")
    public ModelAndView findAllMerchants() {
        List<MerchantDto> merchants = merchantService.findAllMerchants();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("merchants", merchants);
        modelAndView.setViewName("merchant/merchant-display");

        return modelAndView;
    }


    @GetMapping ("/edit/{id}")
    public ModelAndView editMerchant(MerchantDto editedMerchant, ModelAndView modelAndView,
                                     @PathVariable("id")  String id,
                                     @ModelAttribute MerchantDto existingMerchant) {
        existingMerchant = merchantService.findById(id);

        modelAndView.addObject("merchant", editedMerchant);
        modelAndView.addObject("existingMerchant", existingMerchant);
        modelAndView.addObject("id",id);
        modelAndView.setViewName("merchant/merchant-form");

        return modelAndView;
    }


    @PostMapping("/save")
    public ModelAndView saveMerchant( @SessionAttribute("existingMerchant") MerchantDto existingMerchant,
                                      MerchantDto editedMerchant,
                                      ModelAndView modelAndView) throws MerchantException {
       boolean isEdited = merchantService.editMerchant(existingMerchant,editedMerchant);
       if(isEdited){
           modelAndView.setViewName("redirect:/alerts/successAlert");
       }else {
           modelAndView.setViewName("redirect:/alerts/errorAlert");
       }
       return modelAndView;

    }

    @PostMapping("/delete/{id}")
    public ModelAndView deleteMerchant( @ModelAttribute("existingMerchant") MerchantDto existingMerchant,
                                        ModelAndView modelAndView,
                                        @PathVariable("id")  String id) {
        existingMerchant = merchantService.findById(id);

        if(existingMerchant.getTransactions().isEmpty()){
            merchantService.deleteMerchant(existingMerchant);
            modelAndView.setViewName("redirect:/alerts/successAlert");
        }else {
            modelAndView.setViewName("redirect:/alerts/errorAlert");
        }
        return modelAndView;

    }
    @PostMapping("/importMerchants")
    public List<MerchantDto> importMerchants(@RequestBody List<MerchantDto> merchants) throws MerchantException, TransactionException {
        return merTransService.handleMerchants(merchants);
    }

    @ModelAttribute("existingMerchant")
    public MerchantDto merchantDto() {
        return new MerchantDto();
    }

}
