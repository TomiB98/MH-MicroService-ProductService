package com.example.product_service.services;

import com.example.product_service.dtos.NewProduct;
import com.example.product_service.dtos.ProductDTO;
import com.example.product_service.dtos.UpdateProduct;
import com.example.product_service.exceptions.NoProductsFoundException;
import com.example.product_service.models.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    ProductEntity getProductById(Long id) throws NoProductsFoundException;
    ProductDTO getProductDTOById(Long id) throws NoProductsFoundException;
    List<ProductDTO> getAllProducts() throws NoProductsFoundException;

    void createNewProduct (NewProduct newProduct) throws Exception;
    ProductEntity saveProduct(ProductEntity newProduct);

    ProductDTO updateProductById(UpdateProduct updateProduct, Long id) throws Exception;
}
