package org.uhhigor.siitask.exception.product;

public class ProductPricesEmptyException extends ProductException{
    public ProductPricesEmptyException(String message) {
        super(message);
    }

    public ProductPricesEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}
