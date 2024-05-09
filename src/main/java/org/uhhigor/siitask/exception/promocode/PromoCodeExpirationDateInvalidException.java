package org.uhhigor.siitask.exception.promocode;

public class PromoCodeExpirationDateInvalidException extends PromoCodeException{
    public PromoCodeExpirationDateInvalidException(String message) {
        super(message);
    }

    public PromoCodeExpirationDateInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
