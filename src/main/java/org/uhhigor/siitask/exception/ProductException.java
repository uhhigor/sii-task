package org.uhhigor.siitask.exception;

public class ProductException extends SiiTaskException{
    public ProductException(String message) {
        super(message);
    }

    public ProductException(String message, Throwable cause) {
        super(message, cause);
    }
}
