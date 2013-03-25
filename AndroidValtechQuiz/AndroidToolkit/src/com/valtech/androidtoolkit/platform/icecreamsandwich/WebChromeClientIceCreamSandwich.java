package com.valtech.androidtoolkit.platform.icecreamsandwich;

import android.annotation.TargetApi;
import android.view.View;
import android.webkit.WebChromeClient;

import com.valtech.androidtoolkit.platform.froyo.WebChromeClientFroyo;

@TargetApi(14)
public class WebChromeClientIceCreamSandwich extends WebChromeClientFroyo
{
    public WebChromeClientIceCreamSandwich(WebChromeClient pWrapped) {
        super(pWrapped);
    }

    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        mWrapped.onShowCustomView(view, requestedOrientation, callback);
    }
}
