package org.uhhigor.siitask.exception.product;

import org.uhhigor.siitask.exception.SiiTaskException;

public class ProductException extends SiiTaskException {
    public ProductException(String message) {
        super(message);
    }

    public ProductException(String message, Throwable cause) {
        super(message, cause);
    }
}
