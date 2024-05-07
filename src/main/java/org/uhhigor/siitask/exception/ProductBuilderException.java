package org.uhhigor.siitask.exception;

public class ProductBuilderException extends ProductException{
    public ProductBuilderException(String message) {
        super(message);
    }

    public ProductBuilderException(String message, Throwable cause) {
        super(message, cause);
    }
}
