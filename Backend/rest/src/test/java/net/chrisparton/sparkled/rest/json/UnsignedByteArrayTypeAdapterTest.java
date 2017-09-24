package net.chrisparton.sparkled.rest.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UnsignedByteArrayTypeAdapterTest {

    private Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(byte[].class, new UnsignedByteArrayTypeAdapter())
            .create();

    @Test
    public void serialize() throws Exception {
        byte[] bytes = new byte[]{0, 1, 127, -128, -2, -1};
        String json = gson.toJson(bytes);
        assertThat(json, is("[0,1,127,128,254,255]"));
    }
}