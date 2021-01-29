package com.seeker.fitness.all.ex;

public class IllegalTokenException extends ServiceException {

    private static final long serialVersionUID = -6766654818113023102L;

    public IllegalTokenException() {
        super();
    }

    public IllegalTokenException(String message) {
        super(message);
    }

    public IllegalTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalTokenException(Throwable cause) {
        super(cause);
    }

    protected IllegalTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
