package org.uhhigor.siitask.exception.promocode;

public class PromoCodeDiscountInvalidException extends PromoCodeException{
    public PromoCodeDiscountInvalidException(String message) {
        super(message);
    }

    public PromoCodeDiscountInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
