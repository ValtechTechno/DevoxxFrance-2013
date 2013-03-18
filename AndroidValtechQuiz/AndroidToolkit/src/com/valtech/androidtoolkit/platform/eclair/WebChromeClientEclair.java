package com.valtech.androidtoolkit.platform.eclair;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Message;
import android.view.View;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;

@TargetApi(7)
public class WebChromeClientEclair extends WebChromeClient
{
    protected WebChromeClient mWrapped;


    public WebChromeClientEclair(WebChromeClient pWrapped) {
        super();
        mWrapped = pWrapped;
    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        return mWrapped.getDefaultVideoPoster();
    }

    @Override
    public View getVideoLoadingProgressView() {
        return mWrapped.getVideoLoadingProgressView();
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        mWrapped.getVisitedHistory(callback);
    }

    @Override
    public void onCloseWindow(WebView window) {
        mWrapped.onCloseWindow(window);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        mWrapped.onConsoleMessage(message, lineNumber, sourceID);
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        return mWrapped.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    @Override
    public void onExceededDatabaseQuota(String url,
                                        String databaseIdentifier,
                                        long currentQuota,
                                        long estimatedSize,
                                        long totalUsedQuota,
                                        QuotaUpdater quotaUpdater) {
        // To enable HTML5 SQL Datastore, uncomment the following piece of code.
        // quotaUpdater.updateQuota(estimatedSize * 2);
        mWrapped.onExceededDatabaseQuota(url, databaseIdentifier, currentQuota, estimatedSize, totalUsedQuota, quotaUpdater);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        mWrapped.onGeolocationPermissionsHidePrompt();
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
        mWrapped.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    @Override
    public void onHideCustomView() {
        mWrapped.onHideCustomView();
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return mWrapped.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        return mWrapped.onJsBeforeUnload(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return mWrapped.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return mWrapped.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsTimeout() {
        return mWrapped.onJsTimeout();
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        mWrapped.onProgressChanged(view, newProgress);
    }

    @Override
    public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, QuotaUpdater quotaUpdater) {
        mWrapped.onReachedMaxAppCacheSize(spaceNeeded, totalUsedQuota, quotaUpdater);
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        mWrapped.onReceivedIcon(view, icon);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        mWrapped.onReceivedTitle(view, title);
    }

    @Override
    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        mWrapped.onReceivedTouchIconUrl(view, url, precomposed);
    }

    @Override
    public void onRequestFocus(WebView view) {
        mWrapped.onRequestFocus(view);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        mWrapped.onShowCustomView(view, callback);
    }
}
