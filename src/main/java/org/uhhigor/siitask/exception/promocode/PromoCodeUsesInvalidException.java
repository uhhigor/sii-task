package org.uhhigor.siitask.exception.promocode;

public class PromoCodeUsesInvalidException extends PromoCodeException{
    public PromoCodeUsesInvalidException(String message) {
        super(message);
    }

    public PromoCodeUsesInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
