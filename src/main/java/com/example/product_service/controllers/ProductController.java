package com.example.product_service.controllers;

import com.example.product_service.dtos.*;
import com.example.product_service.exceptions.AllBlanksException;
import com.example.product_service.exceptions.NoProductsFoundException;
import com.example.product_service.exceptions.ProductPriceException;
import com.example.product_service.exceptions.StockException;
import com.example.product_service.services.ProductService;
import com.example.product_service.services.TokenDataServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private TokenDataServiceImpl tokenDataService;

    @GetMapping("/")
    public ResponseEntity<String> invalidPath() {
        return ResponseEntity.badRequest().body("The url provided is invalid.");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets a product data with the id", description = "Receives an id and returns all the data of the specified product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data successfully received."),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid id.")
    })
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


    @GetMapping("/name/{id}")
    @Operation(summary = "Gets a product name with the id", description = "Receives an id and returns the specified name of the product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data successfully received."),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid id.")
    })
    public ResponseEntity<String> getProductName(@PathVariable Long id) {
        try {
            String productName = productService.getNameById(id);
            return ResponseEntity.ok(productName);

        } catch (NoProductsFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
        return new ResponseEntity<>("An error occurred while searching the product data, try again later.", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    @GetMapping("/price/{id}")
    @Operation(summary = "Gets a product price with the id", description = "Receives an id and returns the specified price of the product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data successfully received."),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid id.")
    })
    public ResponseEntity<Object> getProductPrice(@PathVariable Long id) {
        try {
            Double productPrice = productService.getPriceById(id);
            return ResponseEntity.ok(productPrice);

        } catch (NoProductsFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while searching the product data, try again later.", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    @GetMapping("/stock/{id}")
    @Operation(summary = "Gets a product stock with the id", description = "Receives an id and returns the specified stock of the product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data successfully received."),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid id.")
    })
    public ResponseEntity<?> getProductStock(@PathVariable Long id) {
        try {
            Integer stock = productService.getProductStockById(id);
            return ResponseEntity.ok(stock);
        } catch (NoProductsFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching the product stock.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}/reduce-stock")
    @Operation(summary = "Reduce a product stock with the id", description = "Receives an id and reduce the specified product stock a determinated amount.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data successfully received."),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid id.")
    })
    public ResponseEntity<?> reduceStock(@PathVariable Long id, @RequestParam Integer quantity) {
        try {
            productService.reduceStock(id, quantity);
            return ResponseEntity.ok("Stock updated successfully.");

        } catch (NoProductsFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (StockException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Error updating stock.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/products")
    @Operation(summary = "Gets all the products", description = "Returns all the products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data successfully received."),
            @ApiResponse(responseCode = "400", description = "Bad request, pool product empty.")
    })
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
    @Operation(summary = "Creates a new product", description = "Receives a name, description, price, stock and creates a new product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product successfully created."),
            @ApiResponse(responseCode = "403", description = "Unauthorized to create a product."),
            @ApiResponse(responseCode = "409", description = "Bad request, invalid data.")
    })
    public ResponseEntity<?> createNewProduct(@RequestBody NewProduct newProduct, HttpServletRequest request) throws Exception {

        try {

            String authenticatedUserRole = tokenDataService.getRole(request);

            if (!authenticatedUserRole.equals("ADMIN")) {
                return new ResponseEntity<>("Forbidden: You cannot access this data.", HttpStatus.FORBIDDEN);
            }

            productService.createNewProduct(newProduct);
            return new ResponseEntity<>("Product crated succesfully", HttpStatus.CREATED);

        } catch (AllBlanksException | ProductPriceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while creating the product, try again later.", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    @PutMapping("products/{id}")
    @Operation(summary = "Updates a product with the id", description = "Receives an id and updates all or independent product data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product successfully created."),
            @ApiResponse(responseCode = "403", description = "Unauthorized to update product."),
            @ApiResponse(responseCode = "409", description = "Bad request, invalid data.")
    })
    public ResponseEntity<?> updateProductById(@RequestBody UpdateProduct updateProduct, @PathVariable Long id, HttpServletRequest request) throws Exception {

        try {

            String authenticatedUserRole = tokenDataService.getRole(request);

            if (!authenticatedUserRole.equals("ADMIN")) {
                return new ResponseEntity<>("Forbidden: You cannot access this data.", HttpStatus.FORBIDDEN);
            }

            ProductAdminDTO updatedProduct = productService.updateProductById(updateProduct, id);
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
