package com.valtech.androidtoolkit.common.exception;

import java.io.IOException;

public class InvalidAuthentificationException extends IOException
{
    private static final long serialVersionUID = -5526519181481929771L;


    public InvalidAuthentificationException() {
        super("User authentification invalid");
    }
}
