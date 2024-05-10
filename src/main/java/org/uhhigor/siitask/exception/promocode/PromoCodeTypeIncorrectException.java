package org.uhhigor.siitask.exception.promocode;

public class PromoCodeTypeIncorrectException extends PromoCodeException{
    public PromoCodeTypeIncorrectException(String message) {
        super(message);
    }

    public PromoCodeTypeIncorrectException(String message, Throwable cause) {
        super(message, cause);
    }
}
