package edu.mnim2377.gyak_spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    public void paymentMethod() {
        log.info("This is the payment method");
    }
}
