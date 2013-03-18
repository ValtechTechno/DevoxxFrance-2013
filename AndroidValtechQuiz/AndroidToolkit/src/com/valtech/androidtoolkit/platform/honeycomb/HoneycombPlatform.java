package com.valtech.androidtoolkit.platform.honeycomb;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.valtech.androidtoolkit.platform.gingerbread.GingerbreadPlatform;

@TargetApi(11)
public class HoneycombPlatform extends GingerbreadPlatform
{
    public HoneycombPlatform(Context pContext) {
        super(pContext);
    }

    @Override
    public void setupWebView(WebView pWebView, WebChromeClient pWebChromeClient) {
        super.setupWebView(pWebView, pWebChromeClient);

        // The embedding activity is hardware accelerated, which causes glitches and flickering
        // when a webview is inside. To avoid this effect, we disable hardware acceleration for
        // webviews only.
        pWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }
}
