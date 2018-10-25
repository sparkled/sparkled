package io.sparkled.model.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final Gson gson = new GsonBuilder()
            .setDateFormat(DATE_FORMAT)
            .registerTypeHierarchyAdapter(byte[].class, new UnsignedByteArrayTypeAdapter())
            .create();

    public static Gson get() {
        return gson;
    }
}
