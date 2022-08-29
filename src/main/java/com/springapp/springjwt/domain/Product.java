package com.springapp.springjwt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private Long id;
    @Column(name = "product_uuid")
    private String uuid;
    @Column(name = "category")
    private String category;
    @Column(name = "subcategory")
    private String subcategory;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "price")
    private double price;
    @Column(name = "currency")
    private String currency;
    @Column(name = "discount")
    private int discount;
    @Column(name = "likes_count")
    private String likesCount;
    @Column(name = "model")
    private String model;
    @Column(name = "description")
    private String description;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "quantity")
    private int quantity;

    @ManyToMany(mappedBy = "productList")
    @JsonIgnore
    private List<Order> orderList = new ArrayList<>();

}
