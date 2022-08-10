package com.springapp.springjwt.service;

import com.springapp.springjwt.domain.CreditCard;

public interface PaymentService {

    void processOrder(String orderId, CreditCard creditCard) throws Exception;
}
