package com.seeker.fitness.all.ex;

/**
 * 客户端入参异常
 */
public class InputAnomalyException extends ServiceException {

    private static final long serialVersionUID = -2047746677880243972L;

    public InputAnomalyException() {
        super();
    }

    public InputAnomalyException(String message) {
        super(message);
    }

    public InputAnomalyException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputAnomalyException(Throwable cause) {
        super(cause);
    }

    protected InputAnomalyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
