package com.springapp.springjwt.modeldto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UpdateProductDTORequest {

    private String uuid;
    private String category;
    private String subcategory;
    private String productName;
    private double price;
    private String currency;
    private int discount;
    private String likesCount;
    private String model;
    private String description;
    private String imageUrl;
    private int quantity;

}
