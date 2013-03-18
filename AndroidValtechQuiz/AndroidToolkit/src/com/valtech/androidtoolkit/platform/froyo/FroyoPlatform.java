package com.valtech.androidtoolkit.platform.froyo;

import java.io.File;

import android.annotation.TargetApi;
import android.content.Context;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;

import com.valtech.androidtoolkit.platform.eclair.EclairPlatform;

@TargetApi(8)
public class FroyoPlatform extends EclairPlatform
{
    public FroyoPlatform(Context pContext) {
        super(pContext);
    }

    @Override
    public File getLocalStorage() {
        return mContext.getExternalFilesDir(null);
    }

    @Override
    public void setupWebView(WebView pWebView, WebChromeClient pWebChromeClient) {
        super.setupWebView(pWebView, pWebChromeClient);

        // Main WebView settings.
        pWebView.getSettings().setBlockNetworkLoads(false);
        pWebView.getSettings().setPluginState(PluginState.ON);
    }

    @Override
    protected WebChromeClient buildWebCromClient(WebChromeClient pWebChromeClient) {
        return new WebChromeClientFroyo(pWebChromeClient);
    }
}
