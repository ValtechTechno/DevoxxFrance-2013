package com.valtech.androidtoolkit.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONUtil
{
    public static JSONArray getJSONArray(JSONObject jsonObject, String field) {
        JSONArray array = jsonObject.optJSONArray(field);
        if (array == null) {
            array = new JSONArray();
            JSONObject object = jsonObject.optJSONObject(field);
            if (object != null) {
                array.put(jsonObject.optJSONObject(field));
            }
        }
        return array;
    }

    public static String getStringOrArray(JSONObject pObjectJSON, String pProperty, String defaultValue) {
        JSONArray arrayJSON = pObjectJSON.optJSONArray(pProperty);
        // Property is an array.
        if (arrayJSON != null) {
            // Concatenates all values
            if (arrayJSON.length() > 0) {
                StringBuilder result = new StringBuilder(arrayJSON.optString(0, ""));
                for (int i = 1; i < arrayJSON.length(); ++i) {
                    result.append("; ").append(arrayJSON.optString(i, ""));
                }
                return result.toString();
            }
            // Empty array.
            else {
                return null;
            }
        }
        // Property is a single value.
        else {
            return pObjectJSON.optString(pProperty, null);
        }
    }
}
