package org.uhhigor.siitask.exception.promocode;

public class PromoCodeCurrencyInvalidException extends PromoCodeException{
    public PromoCodeCurrencyInvalidException(String message) {
        super(message);
    }

    public PromoCodeCurrencyInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
