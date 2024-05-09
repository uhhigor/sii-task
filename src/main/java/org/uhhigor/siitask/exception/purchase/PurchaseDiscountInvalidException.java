package org.uhhigor.siitask.exception.purchase;

public class PurchaseDiscountInvalidException extends PurchaseException{
    public PurchaseDiscountInvalidException(String message) {
        super(message);
    }

    public PurchaseDiscountInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
