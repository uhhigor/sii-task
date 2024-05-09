package org.uhhigor.siitask.exception.purchase;

public class PurchaseDateInvalidException extends PurchaseException{
    public PurchaseDateInvalidException(String message) {
        super(message);
    }

    public PurchaseDateInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
