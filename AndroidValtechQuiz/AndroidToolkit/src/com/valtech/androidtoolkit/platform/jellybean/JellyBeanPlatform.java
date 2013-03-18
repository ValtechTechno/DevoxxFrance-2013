package com.valtech.androidtoolkit.platform.jellybean;

import android.annotation.TargetApi;
import android.content.Context;

import com.valtech.androidtoolkit.platform.icecreamsandwich.IceCreamSandwichPlatform;

@TargetApi(16)
public class JellyBeanPlatform extends IceCreamSandwichPlatform
{
    public JellyBeanPlatform(Context pContext) {
        super(pContext);
    }
}
