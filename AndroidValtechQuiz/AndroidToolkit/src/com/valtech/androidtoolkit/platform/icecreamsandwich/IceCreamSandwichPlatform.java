package com.valtech.androidtoolkit.platform.icecreamsandwich;

import android.annotation.TargetApi;
import android.content.Context;
import android.webkit.WebChromeClient;

import com.valtech.androidtoolkit.platform.honeycomb.HoneycombPlatform;

@TargetApi(14)
public class IceCreamSandwichPlatform extends HoneycombPlatform
{
    public IceCreamSandwichPlatform(Context pContext) {
        super(pContext);
    }

    @Override
    protected WebChromeClient buildWebCromClient(WebChromeClient pWebChromeClient) {
        return new WebChromeClientIceCreamSandwich(pWebChromeClient);
    }
}
