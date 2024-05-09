package org.uhhigor.siitask.exception.purchase;

public class PurchaseCurrencyNullException extends PurchaseException{
    public PurchaseCurrencyNullException(String message) {
        super(message);
    }

    public PurchaseCurrencyNullException(String message, Throwable cause) {
        super(message, cause);
    }
}
