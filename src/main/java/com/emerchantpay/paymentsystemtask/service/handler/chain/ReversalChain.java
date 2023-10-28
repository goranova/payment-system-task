package com.emerchantpay.paymentsystemtask.service.handler.chain;

import com.emerchantpay.paymentsystemtask.service.handler.ReversalHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReversalChain extends ChainHandler {
    @Autowired
    private ReversalHandler reversalHandler;
    @Override
    public void setChain() {
        authorize.setNextTransition(reversalHandler);
    }
}
