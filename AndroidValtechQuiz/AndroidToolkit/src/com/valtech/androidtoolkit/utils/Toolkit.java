package com.valtech.androidtoolkit.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

public class Toolkit
{
    public static String TITLE_SEPARATOR = "//";
    public static String FROMAPP = "fromApp";
    private static String SUBDOMAIN = "logi242";
    public static Context applicationContext;


    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isLandscape(Activity activity) {
        return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isPortrait(Activity activity) {
        return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static String md5(String s) {
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(), 0, s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }

    public static String getTinyUrl(String fullUrl) {
        String tinyUrl = fullUrl;
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("url", fullUrl));
        String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
        HttpGet httpget = new HttpGet("http://tinyurl.com/api-create.php?" + paramString);
        try {
            HttpResponse response = httpclient.execute(httpget);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            tinyUrl = reader.readLine();
        } catch (UnsupportedEncodingException e) {
            Logger.logError("TinyUrl", e.getMessage());
        } catch (ClientProtocolException e) {
            Logger.logError("TinyUrl", e.getMessage());
        } catch (IOException e) {
            Logger.logError("TinyUrl", e.getMessage());
        }
        return tinyUrl;
    }
}
