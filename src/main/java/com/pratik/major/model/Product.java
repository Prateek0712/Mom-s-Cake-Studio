package com.pratik.major.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="product_id")
    private Long id;
    private  String name;
    private double price;
    private double weight;
    private String description;
    private String imageName;
    @ManyToOne
    @JoinColumn
    private Category category;

    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToMany(mappedBy = "products",cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();
}
