package com.seeker.fitness.all.ex;

public class DataBasesException extends ServiceException {
    private static final long serialVersionUID = -2700017186292850764L;

    public DataBasesException() {
    }

    public DataBasesException(String message) {
        super(message);
    }

    public DataBasesException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataBasesException(Throwable cause) {
        super(cause);
    }

    public DataBasesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
