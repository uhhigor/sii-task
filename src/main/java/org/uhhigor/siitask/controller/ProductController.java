package org.uhhigor.siitask.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uhhigor.siitask.exception.product.ProductNotFoundException;
import org.uhhigor.siitask.exception.product.ProductServiceException;
import org.uhhigor.siitask.model.Product;
import org.uhhigor.siitask.model.ProductPrice;
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
        ProductResponse response = new ProductResponse(products.isEmpty() ? "No products found" : products.size() + " products found", products);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest productRequest) {
        try {
            List<ProductPrice> prices = new ArrayList<>();
            System.out.println(productRequest.toString());
            System.out.println(productRequest.getPrices());
            for (ProductRequest.ProductPriceData price : productRequest.getPrices()) {
                ProductPrice productPrice = productService.addProductPrice(price.getPrice(), price.getCurrency());
                prices.add(productPrice);
            }
            Product product = productService.addProduct(productRequest.getName(), productRequest.getDescription(), prices);
            ProductResponse response = new ProductResponse("Product added successfully", List.of(product));
            return ResponseEntity.ok(response);
        } catch (ProductServiceException e) {
            ProductResponse response = new ProductResponse("Failed to add product: " + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductRequest productRequest, @PathVariable Long id) {
        try {
            List<ProductPrice> prices = new ArrayList<>();
            for (ProductRequest.ProductPriceData price : productRequest.getPrices()) {
                ProductPrice productPrice = productService.addProductPrice(price.getPrice(), price.getCurrency());
                prices.add(productPrice);
            }
            Product product = productService.updateProduct(id, productRequest.getName(), productRequest.getDescription(), prices);
            ProductResponse response = new ProductResponse("Product updated successfully", List.of(product));
            return ResponseEntity.ok(response);
        } catch (ProductServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            ProductResponse response = new ProductResponse("Product found", List.of(product));
            return ResponseEntity.ok(response);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            ProductResponse response = new ProductResponse("Product deleted successfully", null);
            return ResponseEntity.ok(response);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ProductResponse {
        private String message;
        private List<ProductData> products;

        ProductResponse(String message, List<Product> products) {
            this.message = message;
            this.products = new ArrayList<>();
            if(products != null) {
                products.forEach(product -> this.products.add(new ProductData(product)));
            }
        }

        @Getter
        @NoArgsConstructor
        static class ProductData {
            private Long id;
            private String name;
            private String description;
            private List<ProductPriceData> prices;

            public ProductData(Product product) {
                this.id = product.getId();
                this.name = product.getName();
                this.description = product.getDescription();
                this.prices = new ArrayList<>();
                product.getPrices().forEach(price -> this.prices.add(new ProductPriceData(price)));
            }

            @Getter
            @NoArgsConstructor
            @AllArgsConstructor
            static class ProductPriceData {
                private Double price;
                private String currency;

                public ProductPriceData(ProductPrice price) {
                    this.price = price.getPrice();
                    this.currency = price.getCurrency().getCurrencyCode();
                }
            }
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductRequest {
        private String name;
        private String description;
        private List<ProductPriceData> prices;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ProductPriceData {
            private Double price;
            private String currency;
        }
    }

}
