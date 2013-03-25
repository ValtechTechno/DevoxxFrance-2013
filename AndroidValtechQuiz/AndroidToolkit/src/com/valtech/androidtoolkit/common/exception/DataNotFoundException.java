package com.valtech.androidtoolkit.common.exception;

public class DataNotFoundException extends DataException
{
    private static final long serialVersionUID = 4807538049787270698L;

    private Object ref;


    public DataNotFoundException(Object object) {
        super();
        ref = object;
    }

    public Object getRef() {
        return ref;
    }
}
