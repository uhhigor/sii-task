package org.uhhigor.siitask.exception.purchase;

public class PurchaseProductNullException extends PurchaseException{
    public PurchaseProductNullException(String message) {
        super(message);
    }

    public PurchaseProductNullException(String message, Throwable cause) {
        super(message, cause);
    }
}
