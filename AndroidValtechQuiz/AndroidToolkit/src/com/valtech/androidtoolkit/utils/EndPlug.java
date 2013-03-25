package com.valtech.androidtoolkit.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * TODO.
 */
public class EndPlug implements InvocationHandler
{
    @SuppressWarnings("unchecked")
    public static <TType> TType forType(Class<? extends TType> pClass) {
        return (TType) Proxy.newProxyInstance(EndPlug.class.getClassLoader(), new Class[] { pClass }, new EndPlug());
    }

    private EndPlug() {}

    @Override
    public Object invoke(Object pProxy, Method pMethod, final Object[] pArguments) throws Throwable {
        return null;
    }
}
