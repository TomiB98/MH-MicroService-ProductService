package com.example.product_service.controllers;

import com.example.product_service.dtos.NewProduct;
import com.example.product_service.dtos.ProductDTO;
import com.example.product_service.dtos.UpdateProduct;
import com.example.product_service.exceptions.AllBlanksException;
import com.example.product_service.exceptions.NoProductsFoundException;
import com.example.product_service.exceptions.ProductPriceException;
import com.example.product_service.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public ResponseEntity<String> invalidPath() {
        return ResponseEntity.badRequest().body("The url provided is invalid.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) throws NoProductsFoundException {

        try {
            ProductDTO productDTO = productService.getProductDTOById(id);
            return ResponseEntity.ok(productDTO);

        } catch (NoProductsFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while searching the product data, try again later.", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() throws NoProductsFoundException {

        try {
            return ResponseEntity.ok(productService.getAllProducts());

        } catch (NoProductsFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while searching the products data, try again later.", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping("/products")
    public ResponseEntity<?> createNewProduct(@RequestBody NewProduct newProduct) throws Exception {

        try {
            productService.createNewProduct(newProduct);
            return new ResponseEntity<>("Product crated succesfully", HttpStatus.CREATED);

        } catch (AllBlanksException | ProductPriceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while creating the product, try again later.", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    @PutMapping("products/{id}")
    public ResponseEntity<?> updateProductById(@RequestBody UpdateProduct updateProduct, @PathVariable Long id) throws Exception {

        try {
            ProductDTO updatedProduct = productService.updateProductById(updateProduct, id);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedProduct);

        } catch (NoProductsFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (AllBlanksException | ProductPriceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while updating the product, try again later.", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
