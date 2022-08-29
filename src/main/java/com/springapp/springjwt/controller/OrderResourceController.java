package com.springapp.springjwt.controller;

import com.springapp.springjwt.domain.CreditCard;
import com.springapp.springjwt.domain.Order;
import com.springapp.springjwt.domain.Product;
import com.springapp.springjwt.exception.domain.*;
import com.springapp.springjwt.service.OrderService;
import com.springapp.springjwt.service.PaymentService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin("http://localhost:4200")
public class OrderResourceController {
    private final OrderService orderService;
    private final PaymentService paymentService;

    @Autowired
    public OrderResourceController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("/getAll")
    public List<Order> getAllOrders() {

        return orderService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Order> insertOrderDba(@RequestParam String productUuid,
                                                @RequestParam String email, @RequestParam String username) throws InvalidOrderException, ProductNotFoundException {

        orderService.addProductToOrder(productUuid,email,username);

        return new ResponseEntity<>( HttpStatus.OK);
    }

    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderDba(@RequestBody Order order, @RequestBody CreditCard creditCard) throws ProductOutOfStockException, OrderNotFoundException, InvalidOrderException, InvalidCreditCardException, ParseException, InvalidUsernameFormatException {
       paymentService.processOrder(order.getOrderUuid(), creditCard);
       return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/getById")
    public Order getOrderById(@RequestParam String uuid) {

        return orderService.getOrderByUuid(uuid);
    }

    @GetMapping("/deleteById")
    public ResponseEntity<String> deleteOrderByUuid(@RequestParam String uuid) {

            orderService.deleteOrderByUuid(uuid);

            return new ResponseEntity<>(JSONObject.quote("Successfully"), HttpStatus.OK);
    }

    @PostMapping("/addProductToOrder")
    public ResponseEntity<String> addProductToExistingOrder(@RequestParam String productUuid,
                                                            @RequestParam String email, @RequestParam String username) throws InvalidOrderException, ProductNotFoundException {
        orderService.addProductToOrder(productUuid,email,username);
        return new ResponseEntity<>(JSONObject.quote("Successfully added!"), HttpStatus.OK);
    }

    @GetMapping("/removeProductFromOrder")
    public ResponseEntity<String> removeProductFromExistingOrder(@RequestParam String uuidProduct, @RequestParam String uuidOrder, @RequestParam String name) throws OrderNotFoundException {
        orderService.removeProductFromOrder(uuidOrder,uuidProduct,name);

        return new ResponseEntity<>(JSONObject.quote("Successfully removed!"), HttpStatus.OK);
    }

    @GetMapping("/getProductsFromOrder")
    public ResponseEntity<List<Product>> getProductsFromOrder(@RequestParam String name, @RequestParam String email) throws OrderNotFoundException {
     List<Product> products = orderService.getProductsFromOrder(name,email);
        if (products == null){
            throw new OrderNotFoundException("Order not found or products list it's empty!");
        }
        return new ResponseEntity<>(products, HttpStatus.OK);

    }

    @GetMapping("/getOrderId")
    public ResponseEntity<String> getOrderId(@RequestParam String username, @RequestParam String email) throws OrderNotFoundException {
        String orderUuid = orderService.getOrderId(username,email);

        if (orderUuid == null){
            throw new OrderNotFoundException("Order not found!");
        }
        return new ResponseEntity<>(orderUuid, HttpStatus.OK);
    }


}
