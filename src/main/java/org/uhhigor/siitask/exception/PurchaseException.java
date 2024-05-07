package org.uhhigor.siitask.exception;

public class PurchaseException extends SiiTaskException{
    public PurchaseException(String message) {
        super(message);
    }

    public PurchaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
