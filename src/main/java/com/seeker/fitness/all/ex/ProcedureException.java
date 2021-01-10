package com.seeker.fitness.all.ex;


public class ProcedureException extends ServiceException {
    private static final long serialVersionUID = 8682983824044379113L;

    public ProcedureException() {
        super();
    }

    public ProcedureException(String message) {
        super(message);
    }

    public ProcedureException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcedureException(Throwable cause) {
        super(cause);
    }

    protected ProcedureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
