package org.uhhigor.siitask.exception;

public class ServiceException extends SiiTaskException{
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
