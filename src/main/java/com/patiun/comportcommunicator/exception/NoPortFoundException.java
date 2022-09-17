package com.patiun.comportcommunicator.exception;

public class NoPortFoundException extends Exception {

    public NoPortFoundException() {
    }

    public NoPortFoundException(String message) {
        super(message);
    }

    public NoPortFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPortFoundException(Throwable cause) {
        super(cause);
    }

    public NoPortFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
