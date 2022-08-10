package com.springapp.springjwt.service.impl;

import com.springapp.springjwt.constant.ProductConstant;
import com.springapp.springjwt.domain.CreditCard;
import com.springapp.springjwt.domain.Order;
import com.springapp.springjwt.domain.Product;
import com.springapp.springjwt.exception.domain.*;
import com.springapp.springjwt.repository.OrderRepository;
import com.springapp.springjwt.repository.ProductRepository;
import com.springapp.springjwt.service.PaymentService;
import com.springapp.springjwt.utility.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentServiceImpl implements PaymentService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final Validator validator;

    @Transactional
    @Override
    public void processOrder(String orderUuid, CreditCard creditCard) throws ProductOutOfStockException, OrderNotFoundException, InvalidOrderException, InvalidCreditCardException, ParseException, InvalidUsernameFormatException {

        validator.validateOrder(orderUuid);
        validator.validateCreditCard(creditCard);
        orderRepository.updateOrderStatus(orderUuid, true);
        for (Product product : orderRepository.findOrderById(orderUuid).getProductList()) {
            product.setQuantity(product.getQuantity() - 1);
            productRepository.save(product);
        }

    }


}
