package org.uhhigor.siitask.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uhhigor.siitask.builder.ProductBuilder;
import org.uhhigor.siitask.builder.ProductPriceBuilder;
import org.uhhigor.siitask.exception.product.ProductException;
import org.uhhigor.siitask.exception.product.ProductNotFoundException;
import org.uhhigor.siitask.exception.product.ProductPriceException;
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
        ProductResponse response = new ProductResponse(products.size() + " products found", products);
        return ResponseEntity.ok(response);
    }

    List<ProductPrice> requestToProductPriceList(List<ProductRequest.ProductPriceData> productPrices) throws ProductPriceException {
        List<ProductPrice> prices = new ArrayList<>();
        for (ProductRequest.ProductPriceData price : productPrices) {
            try {
                ProductPrice productPrice = new ProductPriceBuilder()
                        .price(price.getPrice())
                        .currency(price.getCurrency())
                        .build();
                prices.add(productPrice);
            } catch (ProductException e) {
                throw new ProductPriceException("Error creating ProductPrice: " + e.getMessage());
            }
        }
        return prices;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest productRequest) {
        if(productRequest.getPrices() == null) {
            return ResponseEntity.badRequest().body(new ProductResponse("Error while adding new product: Prices cannot be null or empty"));
        }
        List<ProductPrice> prices;
        try {
            prices = requestToProductPriceList(productRequest.getPrices());
        } catch (ProductPriceException e) {
            return ResponseEntity.badRequest().body(new ProductResponse("Error while adding new product: " + e.getMessage()));
        }
        try {
            Product product = new ProductBuilder()
                    .name(productRequest.getName())
                    .description(productRequest.getDescription())
                    .prices(prices)
                    .build();
            product = productService.addProduct(product);
            return ResponseEntity.ok(new ProductResponse("Product added successfully", List.of(product)));
        } catch (ProductException e) {
            return ResponseEntity.badRequest().body(new ProductResponse("Error while adding new product: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductRequest productRequest, @PathVariable Long id) {
        if(productRequest.getPrices() == null) {
            return ResponseEntity.badRequest().body(new ProductResponse("Error while updating product: Prices cannot be null or empty"));
        }
        List<ProductPrice> prices;
        try {
            prices = requestToProductPriceList(productRequest.getPrices());
        } catch (ProductPriceException e) {
            return ResponseEntity.badRequest().body(new ProductResponse("Error while updating product: " + e.getMessage()));
        }
        try {
            Product product = new ProductBuilder()
                    .name(productRequest.getName())
                    .description(productRequest.getDescription())
                    .prices(prices)
                    .build();
            product = productService.updateProduct(id, product);
            return ResponseEntity.ok(new ProductResponse("Product updated successfully", List.of(product)));
        } catch (ProductServiceException | ProductException e) {
            return ResponseEntity.badRequest().body(new ProductResponse("Error while updating product: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            ProductResponse response = new ProductResponse(List.of(product));
            return ResponseEntity.ok(response);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            ProductResponse response = new ProductResponse("Product deleted successfully");
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

        public ProductResponse(String message) {
            this.message = message;
        }

        public ProductResponse(List<Product> products) {
            this.products = new ArrayList<>();
            products.forEach(product -> this.products.add(new ProductData(product)));
        }

        ProductResponse(String message, List<Product> products) {
            this.message = message;
            this.products = new ArrayList<>();
            products.forEach(product -> this.products.add(new ProductData(product)));
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
