package com.springapp.springjwt.service.impl;

import com.springapp.springjwt.domain.Order;
import com.springapp.springjwt.domain.Product;
import com.springapp.springjwt.domain.User;
import com.springapp.springjwt.exception.domain.InvalidOrderException;
import com.springapp.springjwt.exception.domain.OrderNotFoundException;
import com.springapp.springjwt.exception.domain.ProductNotFoundException;
import com.springapp.springjwt.exception.domain.ProductOutOfStockException;
import com.springapp.springjwt.repository.OrderRepository;
import com.springapp.springjwt.repository.ProductRepository;
import com.springapp.springjwt.service.OrderService;
import com.springapp.springjwt.service.ProductService;
import com.springapp.springjwt.utility.Validator;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements OrderService {
    private final Validator validator;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderByUuid(String uuid) {
        return orderRepository.findOrderById(uuid);
    }

    @Override
    public void deleteOrderByUuid(String uuid) {
        orderRepository.deleteOrderById(uuid);
    }

    @Override
    public boolean removeProductFromOrder(String orderUuid, String productUuid, String name) throws OrderNotFoundException {
        Order order = getOrderByUuid(orderUuid);
        if (order == null){
           throw new OrderNotFoundException("ORDER NOT FOUND");
        }

        for (Product product: order.getProductList()){
            if (product.getUuid().equals(productUuid)) {
                order.getProductList().remove(product);
                orderRepository.save(order);
                return true;
            }
        }

        return false;
    }


    @Override
    public void addProductToOrder(String productUuid, String email, String username) throws ProductNotFoundException, InvalidOrderException {

        //Check if exist an order
        Order order = orderRepository.findOrderByNameAndStatus(username, false,email);



        if (order == null){
            //Create new order with received product

            validator.validateNewOrder(username,Arrays.asList(productUuid));
            List<Product> products = productService.getProductsById(Arrays.asList(productUuid));
            double totalAmount = products.stream().mapToDouble(Product::getPrice).sum();

            Order order1 = new Order();
            order1.setOrderUuid(UUID.randomUUID().toString());
            order1.setLocalDateTime(LocalDateTime.now());
            order1.setUserName(username);
            order1.setProductList(products);
            order1.setTotalAmount(totalAmount);
            order1.setUserEmail(email);
            order1.setPayed(false);
            orderRepository.save(order1);

        }else {

            validator.validateNewOrder(username, Collections.singletonList(productUuid));
            order.getProductList().add(productService.getProductByUuid(productUuid));
            orderRepository.save(order);
        }
    }

    @Override
    public List<Product> getProductsFromOrder(String name, String email) {
        return orderRepository.findOrderByNameAndStatus(name,false,email).getProductList();
    }

    @Override
    public String getOrderId(String name, String email) {
        Order order = orderRepository.findOrderByNameAndStatus(name,false,email);

       if (order == null)
            return null;
        else
            return order.getOrderUuid();
    }
}
