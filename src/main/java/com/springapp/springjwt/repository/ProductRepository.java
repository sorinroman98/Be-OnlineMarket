package com.springapp.springjwt.repository;

import com.springapp.springjwt.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("FROM Product WHERE uuid = ?1")
    public Product findByUuid(String uuid);

    @Query("FROM Product WHERE productName = ?1")
    public Product findByProductName(String productName);

    @Modifying
    @Query("update Product u set u.quantity = u.quantity - 1 where u.uuid = :uuid")
    void updateProductQuantity(@Param(value = "uuid") String uuid);


    @Query("FROM Product WHERE id = ?1")
    Product findById(int id);
}
