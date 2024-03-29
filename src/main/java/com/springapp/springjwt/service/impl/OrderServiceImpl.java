package com.springapp.springjwt.service.impl;

import com.springapp.springjwt.domain.Order;
import com.springapp.springjwt.domain.Product;
import com.springapp.springjwt.exception.domain.InvalidOrderException;
import com.springapp.springjwt.exception.domain.OrderNotFoundException;
import com.springapp.springjwt.exception.domain.ProductNotFoundException;
import com.springapp.springjwt.repository.OrderRepository;
import com.springapp.springjwt.repository.ProductRepository;
import com.springapp.springjwt.repository.UserRepository;
import com.springapp.springjwt.service.OrderService;
import com.springapp.springjwt.service.ProductService;
import com.springapp.springjwt.utility.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements OrderService {
    private final Validator validator;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final UserRepository userRepository;

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderByUuid(String uuid) {
        return orderRepository.findOrderById(uuid);
    }

    @Override
    @Transactional
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
    public void addProductToOrder(String productUuid, String username) throws ProductNotFoundException, InvalidOrderException {

        //Check if exist an order
        Order order = orderRepository.findOrderByNameAndStatus(username, false);
        String email = userRepository.findByUsername(username).getEmail();


        if (order == null){
            //Create new order with received product if it's note exist an order, or existing order is noy paid

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
//            order.getProductList().stream()
//                    .filter( a -> a.getUuid().compareTo(productUuid) == 0)
//                    .findAny()
//                    .ifPresent(a -> a.setQuantity(a.getQuantity()+1));

            order.getProductList().add(productService.getProductByUuid(productUuid));
            orderRepository.save(order);
        }
    }

    @Override
    public List<Product> getProductsFromOrder(String name) {
        return orderRepository.findOrderByNameAndStatus(name,false).getProductList();
    }

    @Override
    public String getOrderId(String name) {
        Order order = orderRepository.findOrderByNameAndStatus(name,false);

       if (order == null)
            return null;

            return order.getOrderUuid();
    }
}
