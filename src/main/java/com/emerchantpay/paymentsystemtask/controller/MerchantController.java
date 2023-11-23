package com.emerchantpay.paymentsystemtask.controller;

import com.emerchantpay.paymentsystemtask.dto.MerchantDto;
import com.emerchantpay.paymentsystemtask.enums.Message;
import com.emerchantpay.paymentsystemtask.exceptions.MerchantException;
import com.emerchantpay.paymentsystemtask.files.csv.CsvHelper;
import com.emerchantpay.paymentsystemtask.response.ResponseMessage;
import com.emerchantpay.paymentsystemtask.service.MerchantService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MerchantController {

    Log log = LogFactory.getLog(this.getClass());

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
    public List<MerchantDto> importMerchants(@RequestBody List<MerchantDto> merchants) throws MerchantException {

        List<MerchantDto> processedMerchants=new ArrayList<>();
        for (MerchantDto mer:merchants){
           MerchantDto processedMer = merchantService.processMerchant(mer);
           processedMerchants.add(processedMer);
        }
        return processedMerchants;
    }


    @PostMapping("/merchants/files")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) throws MerchantException {

        if (CsvHelper.hasCSVFormat(file)) {
            String message;
            try {
                merchantService.save(file);
                message = String.format(Message.UPLOAD_FILE_SUCCESSFULLY.getName(), file.getOriginalFilename());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (IOException e) {
                log.error(e.getMessage());
                message = String.format(Message.COULD_NOT_UPLOAD_FILE.getName(), file.getOriginalFilename());
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(Message.UPLOAD_CSV_FILE.getName()));
    }
}
