package org.uhhigor.siitask.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uhhigor.siitask.exception.product.ProductNotFoundException;
import org.uhhigor.siitask.exception.product.ProductServiceException;
import org.uhhigor.siitask.model.Product;
import org.uhhigor.siitask.service.ProductService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ProductResponse> getProducts() {
        List<Product> products = productService.getProducts();
        List<Product.ProductDto> result = new ArrayList<>();
        products.forEach(product -> result.add(new Product.ProductDto(product)));
        ProductResponse response = new ProductResponse(result.isEmpty() ? "No products found" : result.size() + " products found", result);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@RequestBody Product.ProductDto productDto) {
        try {
            Product product = productService.addProduct(productDto);
            ProductResponse response = new ProductResponse("Product added successfully", List.of(new Product.ProductDto(product)));
            return ResponseEntity.ok(response);
        } catch (ProductServiceException e) {
            ProductResponse response = new ProductResponse("Failed to add product: " + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody Product.ProductDto productDto, @PathVariable Long id) {
        try {
            Product product = productService.updateProduct(productDto, id);
            ProductResponse response = new ProductResponse("Product updated successfully", List.of(new Product.ProductDto(product)));
            return ResponseEntity.ok(response);
        } catch (ProductServiceException e) {
            ProductResponse response = new ProductResponse("Failed to update product: " + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            ProductResponse response = new ProductResponse("Product found", List.of(new Product.ProductDto(product)));
            return ResponseEntity.ok(response);
        } catch (ProductNotFoundException e) {
            ProductResponse response = new ProductResponse("Product not found", null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            ProductResponse response = new ProductResponse("Product deleted successfully", null);
            return ResponseEntity.ok(response);
        } catch (ProductNotFoundException e) {
            ProductResponse response = new ProductResponse("Product not found", null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductResponse {
        private String message;
        private List<Product.ProductDto> product;
    }

}
