package com.springapp.springjwt.service;

import com.springapp.springjwt.domain.Product;
import com.springapp.springjwt.exception.domain.ProductNotFoundException;

import java.util.List;

public interface ProductService {
    Product save(Product product);

    List<Product> getAll();

    List<Product> getProductsById(List<String> idProducts) throws ProductNotFoundException;

    Product getProductByUuid(String uuid);

    public Product editProduct(Product product) throws ProductNotFoundException;
}
