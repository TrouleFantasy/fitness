package com.seeker.fitness.all.ex;

/**
 * 业务异常，是所有业务层异常的基类
 * @author zjh
 *
 */
public class ServiceException extends RuntimeException{

    private static final long serialVersionUID = -3612837135954422941L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    protected ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
