package com.valtech.androidtoolkit.common.exception;

public class UnknownServiceException extends RuntimeException
{
    private static final long serialVersionUID = 7075896587022439079L;


    public UnknownServiceException(String pMessage, Object... pArguments) {
        super(String.format(pMessage, pArguments));
    }
}
