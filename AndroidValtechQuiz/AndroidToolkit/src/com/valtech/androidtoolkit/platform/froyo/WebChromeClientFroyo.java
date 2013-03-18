package com.valtech.androidtoolkit.platform.froyo;

import android.annotation.TargetApi;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

import com.valtech.androidtoolkit.platform.eclair.WebChromeClientEclair;

@TargetApi(8)
public class WebChromeClientFroyo extends WebChromeClientEclair
{
    public WebChromeClientFroyo(WebChromeClient pWrapped) {
        super(pWrapped);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return mWrapped.onConsoleMessage(consoleMessage);
    }
}
