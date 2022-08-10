package com.springapp.springjwt.controller;

import com.springapp.springjwt.domain.Product;
import com.springapp.springjwt.exception.domain.ProductNotFoundException;
import com.springapp.springjwt.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product")
public class ProductResourceController {


    private final ProductService productService;

    @Autowired
    public ProductResourceController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Product> insertProductDb(@RequestBody Product product) {
        product.setUuid(UUID.randomUUID().toString());
        return new ResponseEntity<>(productService.save(product), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public List<Product> returnProductListDb() {

        return productService.getAll();
    }

    @GetMapping("/getByUuid")
    public Product returnProductDb(@RequestParam String uuid) {

        return productService.getProductByUuid(uuid);

    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<Product> editProductDb(@RequestBody Product product) throws ProductNotFoundException {

        return new ResponseEntity<>(productService.editProduct(product), HttpStatus.OK);
    }
}
