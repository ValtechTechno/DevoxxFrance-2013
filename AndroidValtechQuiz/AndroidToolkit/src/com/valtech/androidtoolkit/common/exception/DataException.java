package com.valtech.androidtoolkit.common.exception;

public abstract class DataException extends Exception
{
    private static final long serialVersionUID = 7164418879281647091L;


    public DataException() {
        super();
    }

    public DataException(Throwable pThrowable) {
        super(pThrowable);
    }

    public DataException(String pMessage, Object... pArguments) {
        super(String.format(pMessage, pArguments));
    }

    public DataException(Throwable pThrowable, String pMessage, Object... pArguments) {
        super(String.format(pMessage, pArguments), pThrowable);
    }
}
