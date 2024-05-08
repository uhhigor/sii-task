package org.uhhigor.siitask.exception.product;

import org.uhhigor.siitask.exception.ServiceException;

public class ProductServiceException extends ServiceException {
    public ProductServiceException(String message) {
        super(message);
    }

    public ProductServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
