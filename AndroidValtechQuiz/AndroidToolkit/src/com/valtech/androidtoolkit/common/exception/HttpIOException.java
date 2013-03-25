package com.valtech.androidtoolkit.common.exception;

import java.io.IOException;
import java.net.URL;

public class HttpIOException extends IOException
{
    private static final long serialVersionUID = -5526519181481929771L;
    public static final int DEFAULT_CODE = -1;

    private URL url;
    private int code;


    public HttpIOException(URL url) {
        this(url, DEFAULT_CODE);
    }

    public HttpIOException(URL url, int code) {
        this(url, code, null);
    }

    public HttpIOException(URL url, Throwable throwable) {
        this(url, DEFAULT_CODE, throwable);
    }

    public HttpIOException(URL url, int code, Throwable throwable) {
        super(String.format("HTTP exception with code '%1$s'", code), throwable);
        this.url = url;
        this.code = code;
    }

    public URL getUrl() {
        return url;
    }

    public int getCode() {
        return code;
    }
}
