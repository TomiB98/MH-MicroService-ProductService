package com.example.product_service.dtos;

import com.example.product_service.models.ProductEntity;

public class ProductAdminDTO {

    private Long id;
    private String name, productdescription;
    private Double productprice;
    private Integer stock;
    private boolean availability;

    public ProductAdminDTO(ProductEntity product) {
        id = product.getId();
        name = product.getName();
        productdescription = product.getProductdescription();
        productprice = product.getProductprice();
        stock = product.getStock();
        availability = product.isAvailable();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProductdescription() {
        return productdescription;
    }

    public Double getProductprice() {
        return productprice;
    }

    public Integer getStock() {
        return stock;
    }

    public boolean getAvailability() {
        return availability;
    }

}
