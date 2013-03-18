package com.valtech.androidtoolkit.platform.gingerbread;

import android.annotation.TargetApi;
import android.content.Context;

import com.valtech.androidtoolkit.platform.froyo.FroyoPlatform;

@TargetApi(9)
public class GingerbreadPlatform extends FroyoPlatform
{
    public GingerbreadPlatform(Context pContext) {
        super(pContext);
    }

    @Override
    public void setupHTTPConnection() {
        // Nothing needs to be done.
    }
}
