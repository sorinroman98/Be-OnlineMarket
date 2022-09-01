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

    void addProductToOrder(String productUuid, String username) throws ProductNotFoundException, InvalidOrderException;

    boolean removeProductFromOrder(String orderUuid, String username, String productUuid) throws OrderNotFoundException;

    List<Product> getProductsFromOrder(String name);

    String getOrderId(String name);
}
