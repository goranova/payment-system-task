package com.emerchantpay.paymentsystemtask.controller;

import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.exceptions.MerchantException;
import com.emerchantpay.paymentsystemtask.exceptions.TransactionException;
import com.emerchantpay.paymentsystemtask.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MerchantController {

    @Autowired
    MerchantService merchantService;

    @GetMapping("/merchants")
    public ModelAndView findAllMerchants() {
        List<MerchantDto> merchants = merchantService.findAllMerchants();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("merchants", merchants);
        modelAndView.setViewName("merchant/merchant");

        return modelAndView;
    }


    @GetMapping ("/merchants/{id}")
    public ModelAndView editMerchant(MerchantDto editedMerchant, ModelAndView modelAndView,
                                     @PathVariable("id")  String id) {

        MerchantDto existingMerchant = merchantService.findById(id);

        modelAndView.addObject("merchant", editedMerchant);
        modelAndView.addObject("existingMerchant", existingMerchant);
        modelAndView.addObject("id",id);
        modelAndView.setViewName("merchant/merchant-form");

        return modelAndView;
    }

    @PutMapping(value = "/merchants/{id}")
    public ModelAndView updateMerchant( @PathVariable("id") String id,
                                        MerchantDto editedMerchant,
                                        ModelAndView modelAndView,
                                        RedirectAttributes redirectAttributes) throws MerchantException {

        MerchantDto existingMerchant = merchantService.findById(id);

        if(existingMerchant.getIdentifier()==null){
            throw  new MerchantException(Message.MISSING_MERCHANT.getName());
        }

        boolean isEdited = merchantService.editMerchant(existingMerchant,editedMerchant);
        if(isEdited){
            modelAndView.setViewName("redirect:/alerts/successAlert");
        }else {
            modelAndView.setViewName("redirect:/alerts/errorAlert");
            redirectAttributes.addFlashAttribute("message",Message.EDIT_MERCHANT.getName());
        }
        return modelAndView;

    }

    @DeleteMapping("/merchants/{id}")
    public ModelAndView deleteMerchant(ModelAndView modelAndView,
                                        @PathVariable("id")  String id,
                                        RedirectAttributes redirectAttributes) throws MerchantException {

        MerchantDto existingMerchant = merchantService.findById(id);
        if (existingMerchant.getIdentifier()==null){
           throw new MerchantException(Message.MISSING_MERCHANT.getName());
        }

        if(existingMerchant.getTransactions().isEmpty()){
            merchantService.deleteMerchant(existingMerchant);
            modelAndView.setViewName("redirect:/alerts/successAlert");
        }else {
            modelAndView.setViewName("redirect:/alerts/errorAlert");
            redirectAttributes.addFlashAttribute("message",Message.DELETE_MERCHANT.getName());
        }
        return modelAndView;

    }
    @PostMapping("/merchants")
    public List<MerchantDto> importMerchants(@RequestBody List<MerchantDto> merchants) throws MerchantException, TransactionException {

        List<MerchantDto> processedMerchants=new ArrayList<>();
        for (MerchantDto mer:merchants){
           MerchantDto processedMer = merchantService.processMerchant(mer);
           processedMerchants.add(processedMer);
        }
        return processedMerchants;
    }
}
