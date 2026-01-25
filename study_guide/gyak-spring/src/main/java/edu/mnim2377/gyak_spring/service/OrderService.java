package edu.mnim2377.gyak_spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private PaymentService paymentService;

    public void initialize() {
        log.info("initialize OrderService");
    }

    public void reset() {
        log.info("reset orders");
    }

    public void addProduct() {
        log.info("adding product...");
    }

    public void finalizing() {
        log.info("finalizing order");
        paymentService.paymentMethod();
    }
}
