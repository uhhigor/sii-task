package org.uhhigor.siitask.exception.product;

public class ProductPriceNotValidException extends ProductPriceException {
    public ProductPriceNotValidException(String message) {
        super(message);
    }

    public ProductPriceNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
