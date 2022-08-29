package com.springapp.springjwt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "order_table")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "order_uuid")
    private String orderUuid;
    @Column(name = "customer_name")
    private String userName;
    @Column(name = "customer_email")
    private String userEmail;
    @Column(name = "isPayed")
    private boolean isPayed;
    @Column(name = "localDateTime")
    private LocalDateTime localDateTime;
    @Column(name = "external")
    private boolean isExternal;
    @Column(name = "totalAmount")
    private double totalAmount;


    @ManyToMany()
    @JoinTable(
            name = "order_list",
            joinColumns = @JoinColumn(name = "order_Uuid", referencedColumnName = "order_uuid"),
            inverseJoinColumns = @JoinColumn(name = "product_Uuid", referencedColumnName = "product_uuid")
    )
    private List<Product> productList = new ArrayList<>();

    public void addProduct(Product product) {
        productList.add(product);
    }
}