package net.chrisparton.sparkled.rest.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Serialises bytes as unsigned (0-255) instead of signed. Code producing the bytes that will be serialised must take
 * take this conversion into consideration.
 */
public class UnsignedByteArrayTypeAdapter implements JsonSerializer<byte[]> {

    public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray array = new JsonArray();
        for (byte b : src) {
            array.add(b & 0xFF);
        }
        return array;
    }
}
