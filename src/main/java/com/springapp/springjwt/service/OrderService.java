package com.springapp.springjwt.service;

import com.springapp.springjwt.domain.Order;
import com.springapp.springjwt.domain.Product;
import com.springapp.springjwt.exception.domain.InvalidOrderException;
import com.springapp.springjwt.exception.domain.OrderNotFoundException;
import com.springapp.springjwt.exception.domain.ProductNotFoundException;

import java.util.List;

public interface OrderService {

    List<Order> getAll();

    Order getOrderByUuid(String uuid);

    void deleteOrderByUuid(String uuid);

    void addProductToOrder(String productUuid, String email, String username) throws ProductNotFoundException, InvalidOrderException;

    boolean removeProductFromOrder(String orderUuid, String productUuid, String name) throws OrderNotFoundException;

    List<Product> getProductsFromOrder(String name, String email);

    String getOrderId(String name, String email);
}
