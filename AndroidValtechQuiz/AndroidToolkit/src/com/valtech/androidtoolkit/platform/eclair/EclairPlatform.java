package com.valtech.androidtoolkit.platform.eclair;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;

import com.valtech.androidtoolkit.common.exception.InternalException;
import com.valtech.androidtoolkit.platform.Platform;
import com.valtech.androidtoolkit.utils.Logger;

@TargetApi(7)
public class EclairPlatform implements Platform
{
    protected Context mContext;


    public EclairPlatform(Context pContext) {
        mContext = pContext;
    }

    protected int getWebViewCacheSizeInByte() {
        return 8 * 1024 * 1024;
    }

    @Override
    public boolean isInDebugMode() {
        return (mContext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    @Override
    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null) && (networkInfo.isConnected());
    }

    @Override
    public File getLocalStorage() {
        // Cf. http://developer.android.com/guide/topics/data/data-storage.html#filesExternalits for
        // the reason we use this directory.
        String applicationPackage = mContext.getPackageName();
        String separator = File.pathSeparator;
        String applicationDirectory = "Android" + separator + "data/" + separator + applicationPackage + separator + "files";
        return new File(Environment.getExternalStorageDirectory(), applicationDirectory);
    }

    @Override
    public File getLocalStorageSubDirectory(String pSubDirectory) {
        if (pSubDirectory == null) throw InternalException.isNull();
        return new File(getLocalStorage(), pSubDirectory);
    }

    @Override
    public File getDatabaseDirectory(String pDatabaseName) {
        if (pDatabaseName == null) throw InternalException.isNull();
        return new File(getLocalStorage(), pDatabaseName);
    }

    @Override
    public void share(Activity pActivity, String pTitle, String pSubject, String pText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, pSubject);
        intent.putExtra(Intent.EXTRA_TEXT, pText);
        pActivity.startActivity(Intent.createChooser(intent, pTitle));
    }

    @Override
    public void setupHTTPConnection() {
        System.setProperty("http.keepAlive", "false");
    }

    @Override
    public void setupWebView(WebView pWebView, WebChromeClient pWebChromeClient) {
        // Main WebView settings.
        pWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
        pWebView.setDrawingCacheBackgroundColor(Color.TRANSPARENT);
        pWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        // Cache settings.
        String cachePath = mContext.getCacheDir().getAbsolutePath();
        int appCacheMaxSize = 1024 * 1024 * getWebViewCacheSizeInByte();
        pWebView.getSettings().setAllowFileAccess(true);
        pWebView.getSettings().setAppCacheEnabled(true);
        pWebView.getSettings().setAppCacheMaxSize(appCacheMaxSize);
        pWebView.getSettings().setAppCachePath(cachePath);
        pWebView.getSettings().setCacheMode(WebSettings.LOAD_NORMAL);

        pWebView.setWebChromeClient(buildWebCromClient(pWebChromeClient));
    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public void setupWebViewWithJavascript(WebView pWebView, WebChromeClient pWebChromeClient) {
        setupWebView(pWebView, pWebChromeClient);

        pWebView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void setupWebViewWithDataStore(WebView pWebView, WebChromeClient pWebChromeClient, String pDatastoreName) {
        setupWebViewWithJavascript(pWebView, pWebChromeClient);

        // To enable HTML5 SQL Datastore, uncomment the following piece of code.
        // Don't forget to change also WebChromeClientWrapper.onExceededDatabaseQuota().
        File databasePath = new File(mContext.getFilesDir(), pDatastoreName);
        try {
            pWebView.getSettings().setDatabasePath(databasePath.getCanonicalPath());
            pWebView.getSettings().setDomStorageEnabled(true);
            pWebView.getSettings().setDatabaseEnabled(true);
        } catch (IOException e) {
            e.printStackTrace(); // TODO Handle logs.
            pWebView.getSettings().setDomStorageEnabled(false);
            pWebView.getSettings().setDatabaseEnabled(false);
        }
    }

    @Override
    public void resumeWebView(WebView pWebView) {
        try {
            Method method = WebView.class.getMethod("onResume");
            method.invoke(pWebView);
        } catch (NoSuchMethodException eNoSuchMethodException) {
            Logger.error(this, eNoSuchMethodException);
        } catch (IllegalAccessException eIllegalAccessException) {
            Logger.error(this, eIllegalAccessException);
        } catch (InvocationTargetException eInvocationTargetException) {
            Logger.error(this, eInvocationTargetException);
        }
    }

    @Override
    public void pauseWebView(WebView pWebView) {
        try {
            Method method = WebView.class.getMethod("onPause");
            method.invoke(pWebView);
        } catch (NoSuchMethodException eNoSuchMethodException) {
            Logger.error(this, eNoSuchMethodException);
        } catch (IllegalAccessException eIllegalAccessException) {
            Logger.error(this, eIllegalAccessException);
        } catch (InvocationTargetException eInvocationTargetException) {
            Logger.error(this, eInvocationTargetException);
        }
    }

    @Override
    public void stopWebView(WebView pWebView) {
        pWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
    }

    protected WebChromeClient buildWebCromClient(WebChromeClient pWebChromeClient) {
        return new WebChromeClientEclair(pWebChromeClient);
    }
}
