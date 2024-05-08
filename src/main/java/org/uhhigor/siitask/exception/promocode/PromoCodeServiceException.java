package org.uhhigor.siitask.exception.promocode;

public class PromoCodeServiceException extends PromoCodeException{
    public PromoCodeServiceException(String message) {
        super(message);
    }

    public PromoCodeServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
