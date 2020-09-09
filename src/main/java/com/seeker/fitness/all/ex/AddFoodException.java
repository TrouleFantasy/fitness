package com.seeker.fitness.all.ex;

public class AddFoodException extends ServiceException {
    private static final long serialVersionUID = 2364116168556131405L;

    public AddFoodException() {
        super();
    }

    public AddFoodException(String message) {
        super(message);
    }

    public AddFoodException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddFoodException(Throwable cause) {
        super(cause);
    }

    protected AddFoodException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
