package org.uhhigor.siitask.exception.purchase;

public class PurchaseServiceException extends PurchaseException{
    public PurchaseServiceException(String message) {
        super(message);
    }

    public PurchaseServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
