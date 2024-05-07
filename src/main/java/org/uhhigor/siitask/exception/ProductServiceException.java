package org.uhhigor.siitask.exception;

public class ProductServiceException extends ServiceException {
    public ProductServiceException(String message) {
        super(message);
    }

    public ProductServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
