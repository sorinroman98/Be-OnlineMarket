package com.springapp.springjwt.controller;

import com.springapp.springjwt.domain.HttpResponse;
import com.springapp.springjwt.domain.Product;
import com.springapp.springjwt.exception.domain.ProductNotFoundException;
import com.springapp.springjwt.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.springapp.springjwt.controller.UserResourceController.USER_DELETED_SUCCESSFULLY;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/product")
@CrossOrigin("http://localhost:4200")
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

    @DeleteMapping("/delete/{productUuid}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteProduct(@PathVariable("productUuid") String productUuid) throws ProductNotFoundException {
        productService.deleteProduct(productUuid);

        return response(OK, USER_DELETED_SUCCESSFULLY);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        HttpResponse body = new HttpResponse(
                httpStatus.value(),
                httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(Locale.ROOT),
                message.toUpperCase(Locale.ROOT));

        return new ResponseEntity<>(body ,httpStatus);
    }
}
