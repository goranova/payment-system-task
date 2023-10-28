package com.emerchantpay.paymentsystemtask.service.handler.chain;

import com.emerchantpay.paymentsystemtask.service.handler.ChargeHandler;
import com.emerchantpay.paymentsystemtask.service.handler.RefundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeChain extends ChainHandler {
    @Autowired
    private ChargeHandler charge;
    @Autowired
    private RefundHandler refund;

    @Override
    public void setChain() {
        authorize.setNextTransition(charge);
        charge.setNextTransition(refund);
    }



}
