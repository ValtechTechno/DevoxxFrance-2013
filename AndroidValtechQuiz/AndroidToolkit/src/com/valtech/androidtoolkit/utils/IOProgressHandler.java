package com.valtech.androidtoolkit.utils;

public interface IOProgressHandler
{
    /**
     * Always called on the UI thread. TODO Comments
     */
    void onProgress(int pTotalToRead, int pCurrentlyRead);

    /**
     * Always called on the UI thread. TODO Comments
     */
    void onFinished();

    // TODO Doc InvalidDirectoryException, InvalidFileException, IOExcpetion, FileNotFoundException,
    // etc.
    void onError(Exception pException);


    public class IOBaseProgressHandler implements IOProgressHandler
    {
        @Override
        public void onProgress(int pTotalToRead, int pCurrentlyRead) {}

        @Override
        public void onFinished() {}

        @Override
        public void onError(Exception pException) {}
    }
}
