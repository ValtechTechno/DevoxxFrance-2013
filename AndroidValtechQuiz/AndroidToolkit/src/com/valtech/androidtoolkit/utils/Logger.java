package com.valtech.androidtoolkit.utils;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;
import android.util.Log;

public class Logger
{
    private static boolean DEBUG;


    private Logger() {}

    public static boolean isInDebugMode() {
        return DEBUG;
    }

    public static void initialize(Application pApplication) {
        DEBUG = (pApplication.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    public static void logInfo(String tag, String info) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(info)) Log.i(tag, info);
        }
    }

    public static void logDebug(String tag, String debug) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(debug)) Log.d(tag, debug);
        }
    }

    public static void logWarn(String tag, String warn) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(warn)) Log.w(tag, warn);
        }
    }

    public static void logWarn(String tag, String warn, Throwable t) {
        if (DEBUG) {
            if ((!TextUtils.isEmpty(warn)) && (!TextUtils.isEmpty(t.getMessage()))) Log.w(tag, warn, t);
        }
    }

    public static void logError(String tag, String message, Throwable t) {
        if (DEBUG) {
            if ((!TextUtils.isEmpty(message)) && (!TextUtils.isEmpty(t.getMessage()))) Log.e(tag, message, t);
        }
    }

    public static void logError(String tag, Throwable t) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(t.getMessage())) Log.e(tag, "", t);
        }
    }

    public static void logError(String tag, String error) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(error)) Log.e(tag, error);
        }
    }

    public static void debug(Object pSource, String pMessage, Object... pArguments) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(pMessage)) Log.d(pSource.getClass().getSimpleName(), String.format(pMessage, pArguments));
        }
    }

    public static void debug(Class<?> pSource, String pMessage, Object... pArguments) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(pMessage)) Log.d(pSource.getSimpleName(), String.format(pMessage, pArguments));
        }
    }

    public static void info(Object pSource, String pMessage, Object... pArguments) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(pMessage)) Log.i(pSource.getClass().getSimpleName(), String.format(pMessage, pArguments));
        }
    }

    public static void info(Class<?> pSource, String pMessage, Object... pArguments) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(pMessage)) Log.i(pSource.getSimpleName(), String.format(pMessage, pArguments));
        }
    }

    public static void warn(Object pSource, String pMessage, Object... pArguments) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(pMessage)) Log.w(pSource.getClass().getSimpleName(), String.format(pMessage, pArguments));
        }
    }

    public static void warn(Class<?> pSource, String pMessage, Object... pArguments) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(pMessage)) Log.w(pSource.getSimpleName(), String.format(pMessage, pArguments));
        }
    }

    public static void warn(Object pSource, Throwable pThrowable) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(pThrowable.getMessage())) Log.w(pSource.getClass().getSimpleName(), pThrowable);
        }
    }

    public static void warn(Class<?> pSource, Throwable pThrowable) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(pThrowable.getMessage())) Log.w(pSource.getSimpleName(), pThrowable);
        }
    }

    public static void warn(Object pSource, Throwable pThrowable, String pMessage, Object... pArguments) {
        if (DEBUG) {
            if ((!TextUtils.isEmpty(pMessage)) && (!TextUtils.isEmpty(pThrowable.getMessage()))) Log.w(pSource.getClass()
                            .getSimpleName(), String.format(pMessage, pArguments), pThrowable);
        }
    }

    public static void warn(Class<?> pSource, Throwable pThrowable, String pMessage, Object... pArguments) {
        if (DEBUG) {
            if ((!TextUtils.isEmpty(pMessage)) && (!TextUtils.isEmpty(pThrowable.getMessage()))) Log.w(pSource.getSimpleName(),
                                                                                                       String.format(pMessage,
                                                                                                                     pArguments),
                                                                                                       pThrowable);
        }
    }

    public static void error(Object pSource, String pMessage, Object... pArguments) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(pMessage)) Log.e(pSource.getClass().getSimpleName(), String.format(pMessage, pArguments));
        }
    }

    public static void error(Class<?> pSource, String pMessage, Object... pArguments) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(pMessage)) Log.e(pSource.getSimpleName(), String.format(pMessage, pArguments));
        }
    }

    public static void error(Object pSource, Throwable pThrowable) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(pThrowable.getMessage())) Log.e(pSource.getClass().getSimpleName(), "", pThrowable);
        }
    }

    public static void error(Class<?> pSource, Throwable pThrowable) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(pThrowable.getMessage())) Log.e(pSource.getSimpleName(), "", pThrowable);
        }
    }

    public static void error(Object pSource, Throwable pThrowable, String pMessage, Object... pArguments) {
        if (DEBUG) {
            if ((!TextUtils.isEmpty(pMessage)) && (!TextUtils.isEmpty(pThrowable.getMessage()))) Log.e(pSource.getClass()
                            .getSimpleName(), String.format(pMessage, pArguments), pThrowable);
        }
    }

    public static void error(Class<?> pSource, Throwable pThrowable, String pMessage, Object... pArguments) {
        if (DEBUG) {
            if ((!TextUtils.isEmpty(pMessage)) && (!TextUtils.isEmpty(pThrowable.getMessage()))) Log.e(pSource.getSimpleName(),
                                                                                                       String.format(pMessage,
                                                                                                                     pArguments),
                                                                                                       pThrowable);
        }
    }
}
