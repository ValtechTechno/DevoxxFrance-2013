package com.valtech.androidtoolkit.platform;

import java.io.File;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.valtech.androidtoolkit.platform.eclair.EclairPlatform;
import com.valtech.androidtoolkit.platform.froyo.FroyoPlatform;
import com.valtech.androidtoolkit.platform.gingerbread.GingerbreadPlatform;
import com.valtech.androidtoolkit.platform.honeycomb.HoneycombPlatform;
import com.valtech.androidtoolkit.platform.icecreamsandwich.IceCreamSandwichPlatform;

/**
 * Class used to abstract platform-specific set-up, behaviour or anything else. Sub-classes uses an
 * Android Activity context and thus should not be used beyond activity scope.
 */
public interface Platform
{
    static final String UTF8_MIME_TYPE = "text/html";
    static final String UTF8_ENCODING = "utf-8";


    /**
     * Indicates if the application is in debug mode (cf. debuggable attribute in
     * AnroidManifest.xml).
     */
    boolean isInDebugMode();

    /**
     * Indicates if device is connected to the Internet.
     */
    boolean isConnected();

    /**
     * Get the directory of the local storage (e.g. an SD-card or the internal memory for "cardless"
     * device). Note: There is no guarantee the directory exists or is mounted (e.g. SD-Card
     * removed).
     */
    File getLocalStorage();

    /**
     * Get a sub-directory in the local storage. Note: There is no guarantee the directory exists or
     * is mounted (e.g. SD-Card removed).
     */
    File getLocalStorageSubDirectory(String pSubDirectory);

    /**
     * Get the directory where is stored the specified database. Note: There is no guarantee the
     * directory exists or is mounted (e.g. SD-Card removed).
     */
    File getDatabaseDirectory(String pDatabaseName); // TODO Useless??

    /**
     * Share a piece of data. TODO Javadoc
     * @param pActivity
     * @param pTitle
     * @param pSubject
     * @param pText
     */
    void share(Activity pActivity, String pTitle, String pSubject, String pText);

    /**
     * Set system settings to fix bugs in HTTPUrlConnection.
     */
    void setupHTTPConnection();

    /**
     * Setup a WebView in a platform specific way (hardware rendering, etc.)
     */
    void setupWebView(WebView pWebView, WebChromeClient pWebChromeClient);

    /**
     * Setup a WebView with Javascript enabled.
     */
    void setupWebViewWithJavascript(WebView pWebView, WebChromeClient pWebChromeClient);

    /**
     * Same as setupWebviewWithJavascript() but activates the HTML5 SQL Datastore.
     */
    void setupWebViewWithDataStore(WebView pWebView, WebChromeClient pWebChromeClient, String pDatastoreName);

    /**
     * Makes sure the webview is properly paused (because Javascript thread or the Flash plugin may
     * still run, etc.).
     */
    void resumeWebView(WebView pWebView);

    /**
     * Makes sure the webview is properly resumed (because Javascript thread or the Flash plugin may
     * be paussed, etc.).
     */
    void pauseWebView(WebView pWebView);

    /**
     * Makes sure the webview is properly stopped (because Javascript thread or the Flash plugin may
     * still run, etc.).
     */
    void stopWebView(WebView pWebView);


    public static class Factory
    {
        public static Platform findCurrentPlatform(Application pApplication) {
            Context context = pApplication.getApplicationContext();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                return new IceCreamSandwichPlatform(context);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                return new HoneycombPlatform(context);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                return new GingerbreadPlatform(context);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                return new FroyoPlatform(context);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
                return new EclairPlatform(context);
            } else {
                throw new PlatformNotSupported(Build.VERSION.SDK_INT);
            }
        }
    }
}
