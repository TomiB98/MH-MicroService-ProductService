package com.example.product_service.services;

import com.example.product_service.dtos.*;
import com.example.product_service.exceptions.NoProductsFoundException;
import com.example.product_service.exceptions.StockException;
import com.example.product_service.models.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    ProductEntity getProductById(Long id) throws NoProductsFoundException;
    ProductDTO getProductDTOById(Long id) throws NoProductsFoundException;

    List<ProductDTO> getAllProducts() throws NoProductsFoundException;

    String getNameById(Long id) throws NoProductsFoundException;
    Double getPriceById(Long id) throws NoProductsFoundException;

    Integer getProductStockById(Long id) throws NoProductsFoundException;
    void reduceStock(Long productId, Integer quantity) throws NoProductsFoundException, StockException;
    void restockProduct(Long productId, Integer quantity) throws NoProductsFoundException;

    void createNewProduct (NewProduct newProduct) throws Exception;
    ProductEntity saveProduct(ProductEntity newProduct);

    ProductAdminDTO updateProductById(UpdateProduct updateProduct, Long id) throws Exception;

}
