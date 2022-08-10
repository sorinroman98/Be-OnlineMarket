package com.springapp.springjwt.service;

import com.springapp.springjwt.domain.Order;
import com.springapp.springjwt.domain.Product;
import com.springapp.springjwt.domain.User;
import com.springapp.springjwt.exception.domain.OrderNotFoundException;
import org.aspectj.weaver.ast.Or;

import javax.mail.internet.ParseException;
import java.util.List;

public interface OrderService {

    List<Order> getAll();

   // Order createOrderDBA(OrderCreateDto orderRequest);

  //  Order payOrderDba(OrderPayDto orderPayRequest) throws ParseException, java.text.ParseException;

    Order getOrderByUuid(String uuid);

    void deleteOrderByUuid(String uuid);

   // boolean addProductToExistingOrder(AddProductsToOrderRequest addProductsToOrderRequest);

    boolean removeProductFromOrder(String orderUuid, String productUuid, String name) throws OrderNotFoundException;

    List<Product> getProductsFromOrder(String name, String email);

    String getOrderId(String name, String email);
}
