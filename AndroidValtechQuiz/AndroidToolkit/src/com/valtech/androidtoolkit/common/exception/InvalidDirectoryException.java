package com.valtech.androidtoolkit.common.exception;

import java.io.File;
import java.io.IOException;

public class InvalidDirectoryException extends IOException
{
    private static final long serialVersionUID = -8412175732311306286L;

    private File mDirectory;


    public InvalidDirectoryException(File pDirectory) {
        super(String.format("File '%1$s' is not a valid file", pDirectory));
        mDirectory = pDirectory;
    }

    public File getDirectory() {
        return mDirectory;
    }
}
