package org.uhhigor.siitask.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uhhigor.siitask.exception.ProductServiceException;
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
    public ResponseEntity<Object> getProducts() {
        Iterable<Product> products = productService.getProducts();
        List<Product.ProductDto> result = new ArrayList<>();
        for (Product product : products) {
            result.add(new Product.ProductDto(product));
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Object> addProduct(@RequestBody Product.ProductDto productDto) {
        try {
            Product product = productService.addProduct(productDto);
            return ResponseEntity.ok(new Product.ProductDto(product));
        } catch (ProductServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@RequestBody Product.ProductDto productDto, @PathVariable Long id) {
        try {
            Product product = productService.updateProduct(productDto, id);
            return ResponseEntity.ok(new Product.ProductDto(product));
        } catch (ProductServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable Long id) {
        try {
            Product product = productService.getById(id);
            return ResponseEntity.ok(new Product.ProductDto(product));
        } catch (ProductServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } catch (ProductServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
