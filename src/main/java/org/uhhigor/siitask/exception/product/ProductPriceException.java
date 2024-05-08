package org.uhhigor.siitask.exception.product;

import org.uhhigor.siitask.exception.SiiTaskException;

public class ProductPriceException extends SiiTaskException {
    public ProductPriceException(String message) {
        super(message);
    }

    public ProductPriceException(String message, Throwable cause) {
        super(message, cause);
    }
}
