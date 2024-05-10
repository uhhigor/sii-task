package org.uhhigor.siitask.exception.purchase;

public class PurchaseNotFoundException extends PurchaseException{
    public PurchaseNotFoundException(String message) {
        super(message);
    }

    public PurchaseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
