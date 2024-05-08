package org.uhhigor.siitask.exception.product;

public class ProductPriceBuilderException extends ProductPriceException{
    public ProductPriceBuilderException(String message) {
        super(message);
    }

    public ProductPriceBuilderException(String message, Throwable cause) {
        super(message, cause);
    }
}
