package com.springapp.springjwt.service;

import com.springapp.springjwt.domain.CreditCard;
import com.springapp.springjwt.exception.domain.*;

import java.text.ParseException;

public interface PaymentService {

    void processOrder(String orderId, CreditCard creditCard) throws ProductOutOfStockException, OrderNotFoundException, InvalidOrderException, InvalidCreditCardException, ParseException, InvalidUsernameFormatException;
}
