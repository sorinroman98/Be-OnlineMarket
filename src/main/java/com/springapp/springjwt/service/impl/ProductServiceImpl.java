package com.springapp.springjwt.service.impl;

import com.springapp.springjwt.domain.Product;
import com.springapp.springjwt.exception.domain.ProductNotFoundException;
import com.springapp.springjwt.repository.ProductRepository;
import com.springapp.springjwt.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.springapp.springjwt.constant.ProductConstant.PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Product save(Product product) {

        productRepository.save(product);
        return product;
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsById(List<String> idProducts) throws ProductNotFoundException {
        List<Product> products = new ArrayList<>();

        for (String idProduct : idProducts) {
            if (productRepository.findByUuid(idProduct) == null) {
                throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
            }
            products.add(productRepository.findByUuid(idProduct));
        }

        return products;
    }

    @Override
    public Product getProductByUuid(String uuid) {
        return productRepository.findByUuid(uuid);
    }

    @Override
    public Product updateProduct(Product product) throws ProductNotFoundException {
        if(product == null) {
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(String productUuid) throws ProductNotFoundException {
        Product product = productRepository.findByUuid(productUuid);
        if (product != null){
            productRepository.delete(product);
        }
        throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
    }
}
