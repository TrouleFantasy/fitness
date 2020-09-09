package com.seeker.fitness.all.ex;

public class WithIOException extends ServiceException{
    private static final long serialVersionUID = 2375702682208065860L;

    public WithIOException() {
        super();
    }

    public WithIOException(String message) {
        super(message);
    }

    public WithIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public WithIOException(Throwable cause) {
        super(cause);
    }

    protected WithIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
