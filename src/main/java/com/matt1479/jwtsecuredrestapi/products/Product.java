package com.matt1479.jwtsecuredrestapi.products;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity 
public class Product {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank 
    @Column(nullable = false, unique = true, length = 75)
    private String title;

    @NotBlank 
    @Column(nullable = false, length = 750)
    private String description;

    // TODO: use BigDecimal
    @NotNull 
    @Positive 
    @Column(nullable = false)
    private Double price;

    // required by JPA; application code should use the parameterized constructor
    protected Product() {}

    public Product(String title, String description, Double price) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }

        if (price == null) {
            throw new IllegalArgumentException("Price is required");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        this.title = title;
        this.description = description;
        this.price = price;
    }

    @Override 
    public String toString() {
        return String.format(
            "Product=[id=%s, title='%s', description='%s', price=%s]",
            id, title, description, price
        );
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }

        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        if (price == null) {
            throw new IllegalArgumentException("Price is required");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        this.price = price;
    }

}
