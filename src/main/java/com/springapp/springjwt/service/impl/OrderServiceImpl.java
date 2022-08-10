package com.springapp.springjwt.service.impl;

import com.springapp.springjwt.domain.Order;
import com.springapp.springjwt.domain.Product;
import com.springapp.springjwt.exception.domain.OrderNotFoundException;
import com.springapp.springjwt.repository.OrderRepository;
import com.springapp.springjwt.repository.ProductRepository;
import com.springapp.springjwt.service.OrderService;
import com.springapp.springjwt.service.ProductService;
import com.springapp.springjwt.utility.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements OrderService {
    private final transient Validator validator;
    private final transient OrderRepository orderRepository;
    private final transient ProductRepository productRepository;
    private final transient ProductService productService;

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
