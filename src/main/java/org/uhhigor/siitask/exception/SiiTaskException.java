package org.uhhigor.siitask.exception;

public class SiiTaskException extends Exception{
    public SiiTaskException(String message) {
        super(message);
    }

    public SiiTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
