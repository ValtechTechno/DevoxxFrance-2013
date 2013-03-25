package com.valtech.androidtoolkit.platform;

public class PlatformNotSupported extends RuntimeException
{
    private static final long serialVersionUID = 2209766991111542364L;

    private int mPlatformVersion;


    public PlatformNotSupported(int pPlatformVersion) {
        mPlatformVersion = pPlatformVersion;
    }

    @Override
    public String getMessage() {
        return String.format("Platform %1$s is not supported", mPlatformVersion);
    }

    public int getPlatformVersion() {
        return mPlatformVersion;
    }
}
