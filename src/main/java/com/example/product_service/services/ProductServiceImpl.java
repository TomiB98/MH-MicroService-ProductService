package com.example.product_service.services;

import com.example.product_service.dtos.NewProduct;
import com.example.product_service.dtos.ProductDTO;
import com.example.product_service.dtos.UpdateProduct;
import com.example.product_service.exceptions.AllBlanksException;
import com.example.product_service.exceptions.NoProductsFoundException;
import com.example.product_service.exceptions.ProductPriceException;
import com.example.product_service.exceptions.StockException;
import com.example.product_service.models.ProductEntity;
import com.example.product_service.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductEntity getProductById(Long id) throws NoProductsFoundException {
        return productRepository.findById(id).orElseThrow( () -> new NoProductsFoundException("Product with ID " + id + " not found."));
    }


    @Override
    public ProductDTO getProductDTOById(Long id) throws NoProductsFoundException {
        return new ProductDTO(getProductById(id));
    }


    @Override
    public Integer getProductStockById(Long id) throws NoProductsFoundException {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new NoProductsFoundException("Product with ID " + id + " not found."));
        return product.getStock();
    }


    @Override
    public void reduceStock(Long productId, Integer quantity) throws NoProductsFoundException, StockException {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new NoProductsFoundException("Product with ID " + productId + " not found."));

        if (product.getStock() < quantity) {
            throw new StockException("Not enough stock for product with ID " + productId);
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }


    @Override
    public List<ProductDTO> getAllProducts() throws NoProductsFoundException {

        List<ProductDTO> products = productRepository.findAll().stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());

        if (products.isEmpty()) {
            throw new NoProductsFoundException("There are no products.");
        }

        return products;
    }

    @Override
    public void createNewProduct(NewProduct newProduct) throws Exception {
        validateNewProduct(newProduct);
        ProductEntity product = new ProductEntity(newProduct.name(), newProduct.productdescription(), newProduct.productprice(), newProduct.stock());
        saveProduct(product);
    }


    @Override
    public ProductEntity saveProduct(ProductEntity product) {
        return productRepository.save(product);
    }


    @Override
    public ProductDTO updateProductById(UpdateProduct updatedProduct, Long id) throws Exception {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(()-> new NoProductsFoundException("Product with ID " + id + " not found."));

        validateUpdatedProduct(updatedProduct);

        if (!updatedProduct.name().isBlank()) {
            product.setName(updatedProduct.name());
        }

        if (!updatedProduct.productdescription().isBlank()) {
            product.setProductdescription(updatedProduct.productdescription());
        }

        if (updatedProduct.productprice() != null) {
            product.setProductprice(updatedProduct.productprice());
        }

        if (updatedProduct.stock() != null) {
            product.setStock(updatedProduct.stock() );
        }

        productRepository.save(product);
        return new ProductDTO(product);
    }


    //Validations
    public void validateUpdatedProduct (UpdateProduct updatedProduct) throws Exception {
        validateProductPrice(updatedProduct.productprice());
        validateAllBlanks(updatedProduct.name(), updatedProduct.productdescription(), updatedProduct.productprice(), updatedProduct.stock());
        validateStock(updatedProduct.stock());
    }

    public static void validateAllBlanks(String name, String productdescription, Double productprice, Integer stock) throws AllBlanksException {
        if(name.isBlank() && productdescription.isBlank() && productprice == null && stock == null) {
            throw new AllBlanksException("At least one value has to be modified.");
        }
    }

    public void validateNewProduct (NewProduct newProduct) throws Exception {
        validateMissingInfo(newProduct.name(), newProduct.productdescription(), newProduct.productprice(), newProduct.stock());
        validateProductPrice(newProduct.productprice());
        validateStock(newProduct.stock());
    }

    public static void validateMissingInfo(String name, String productdescription, Double productprice, Integer stock) throws AllBlanksException {
        if(name.isBlank() || productdescription.isBlank() || productprice == null || stock == null) {
            throw new AllBlanksException("All fields are required.");
        }
    }

    public static void validateProductPrice(Double productprice) throws ProductPriceException {
        if (productprice == null || productprice.isNaN() || productprice <= 0) {
            throw new ProductPriceException("Invalid price must be a positive number (e.g.: 120.00).");
        }
    }

    public static void validateStock(Integer stock) throws StockException {
        if (stock == null || stock <= 0) {
            throw new StockException("Invalid stock must be a positive number.");
        }
    }
}
