package org.uhhigor.siitask.exception.promocode;

import org.uhhigor.siitask.exception.SiiTaskException;

public class PromoCodeException extends SiiTaskException {
    public PromoCodeException(String message) {
        super(message);
    }

    public PromoCodeException(String message, Throwable cause) {
        super(message, cause);
    }

}

