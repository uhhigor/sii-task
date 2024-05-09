package org.uhhigor.siitask.exception.product;


public class ProductNotFoundException extends ProductServiceException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
