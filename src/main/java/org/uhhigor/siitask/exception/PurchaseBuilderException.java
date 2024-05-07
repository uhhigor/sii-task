package org.uhhigor.siitask.exception;

public class PurchaseBuilderException extends PurchaseException{
    public PurchaseBuilderException(String message) {
        super(message);
    }

    public PurchaseBuilderException(String message, Throwable cause) {
        super(message, cause);
    }
}
