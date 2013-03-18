package com.valtech.androidtoolkit.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SerializationUtil
{
    public static <TKey extends Parcelable, TValue extends Parcelable> void writeMap(Parcel parcel, Map<TKey, TValue> map) {
        Set<TKey> keySet = map.keySet();
        Bundle keyBundle = new Bundle();
        Bundle valueBundle = new Bundle();
        int index = 0;
        for (TKey key : keySet) {
            keyBundle.putParcelable(Integer.toString(index), key);
            valueBundle.putParcelable(Integer.toString(index), map.get(key));
        }
        parcel.writeBundle(keyBundle);
        parcel.writeBundle(valueBundle);
    }

    public static <TKey extends Parcelable, TValue extends Parcelable> Map<TKey, TValue> readMap(Parcel parcel) {
        Map<TKey, TValue> map = Maps.newHashMap();
        Bundle keyBundle = parcel.readBundle();
        Bundle valueBundle = parcel.readBundle();
        int index = 0;
        for (int i = 0; i < keyBundle.size(); ++i) {
            @SuppressWarnings("unchecked")
            TKey key = (TKey) keyBundle.get(Integer.toString(index));
            @SuppressWarnings("unchecked")
            TValue value = (TValue) valueBundle.get(Integer.toString(index));
            map.put(key, value);
        }
        return map;
    }

    public static <TParcelable extends Parcelable> void putParcelableList(Bundle bundle, String key, List<TParcelable> list) {
        bundle.putParcelableArrayList(key, (list instanceof ArrayList) ? (ArrayList<TParcelable>) list : Lists.newArrayList(list));
    }

    public static <TObject extends Object> void putExtra(Intent intent, String key, List<TObject> list) {
        intent.putExtra(key, (list instanceof ArrayList) ? (ArrayList<TObject>) list : Lists.newArrayList(list));
    }
}
