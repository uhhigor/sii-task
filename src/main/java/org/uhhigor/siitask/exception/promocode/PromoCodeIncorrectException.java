package org.uhhigor.siitask.exception.promocode;

public class PromoCodeIncorrectException extends PromoCodeException{
    public PromoCodeIncorrectException(String message) {
        super(message);
    }

    public PromoCodeIncorrectException(String message, Throwable cause) {
        super(message, cause);
    }
}
