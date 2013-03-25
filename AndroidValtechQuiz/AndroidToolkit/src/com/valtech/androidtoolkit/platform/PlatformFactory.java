package com.valtech.androidtoolkit.platform;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.valtech.androidtoolkit.platform.eclair.EclairPlatform;
import com.valtech.androidtoolkit.platform.froyo.FroyoPlatform;
import com.valtech.androidtoolkit.platform.gingerbread.GingerbreadPlatform;
import com.valtech.androidtoolkit.platform.honeycomb.HoneycombPlatform;
import com.valtech.androidtoolkit.platform.icecreamsandwich.IceCreamSandwichPlatform;

public class PlatformFactory
{
    public static Platform getCurrentPlatform(Application pApplication) {
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
